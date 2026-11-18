package com.example.myapplication.ui.telas.jogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.EstadoPartidaCriptograma
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class JogoViewModel(
    private val repositoryUsuario: UsuarioRepository,
    private val repositoryPartida: PartidaRepository,
    private val repositoryCriptograma: CriptogramaRepository,
    criptogramaId: Int?
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        JogoUiState(criptogramaId = criptogramaId, carregando = true)
    )

    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()

    private val mutexSalvar = Mutex()

    init {
        viewModelScope.launch {
            val usuario = repositoryUsuario.garantirUsuarioDefault(Papel.USUARIO)
            val partidas = repositoryPartida.listarTodos().first()
            val idCriptograma = criptogramaId
                ?: partidas.firstOrNull { it.usuarioId == usuario.id && !it.concluida }?.criptogramaId
            val partidaEmAndamento = partidas.firstOrNull {
                it.usuarioId == usuario.id && it.criptogramaId == idCriptograma && !it.concluida
            }
            _uiState.update { currentState ->
                currentState.copy(
                    usuarioId = usuario.id,
                    carregando = false,
                    partidaId = partidaEmAndamento?.id,
                    segundos = partidaEmAndamento?.tempo ?: 0L,
                    erros = partidaEmAndamento?.erros ?: 0,
                    dicasUsadas = partidaEmAndamento?.dicasUsadas ?: 0,
                    originalRevelado = partidaEmAndamento?.originalRevelado ?: false,
                    palpites = partidaEmAndamento
                        ?.let { EstadoPartidaCriptograma.desserializar(it.estadoAtual) }
                        ?: emptyMap()
                )
            }
        }

        viewModelScope.launch {
            repositoryPartida.listarTodos().collect { partidas ->
                _uiState.update { currentState ->
                    currentState.copy(partidas = partidas)
                }
            }
        }

        viewModelScope.launch {
            repositoryCriptograma.listarTodos().collect { criptogramas ->
                _uiState.update { currentState ->
                    currentState.copy(criptogramas = criptogramas)
                }
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { currentState ->
                    if (currentState.podeInteragir) {
                        currentState.copy(segundos = currentState.segundos + 1)
                    } else {
                        currentState
                    }
                }
            }
        }
    }

    fun onCelulaClick(letraCifrada: Char) {
        _uiState.update { currentState ->
            if (!currentState.podeInteragir) return@update currentState
            currentState.copy(letraCifradaSelecionada = letraCifrada)
        }
    }

    fun onLetraDigitada(letra: Char) {
        _uiState.update { currentState ->
            val cifrada = currentState.letraCifradaSelecionada ?: return@update currentState
            val original = currentState.respostas[cifrada] ?: return@update currentState
            if (!currentState.podeInteragir || currentState.palpites[cifrada] == original) {
                return@update currentState
            }

            val errou = letra != original
            val atualizado = currentState.copy(
                palpites = currentState.palpites + (cifrada to letra),
                erros = if (errou && currentState.palpites[cifrada] != letra) {
                    currentState.erros + 1
                } else {
                    currentState.erros
                }
            )
            atualizado.copy(
                letraCifradaSelecionada = if (errou) cifrada else atualizado.proximaLetraPendente(cifrada)
            )
        }
        salvarSeDecifrado()
    }

    fun onApagarClick() {
        _uiState.update { currentState ->
            val cifrada = currentState.letraCifradaSelecionada ?: return@update currentState
            if (!currentState.podeInteragir || currentState.palpites[cifrada] == currentState.respostas[cifrada]) {
                return@update currentState
            }
            currentState.copy(palpites = currentState.palpites - cifrada)
        }
    }

    fun onPausarClick(onSalvo: () -> Unit) {
        viewModelScope.launch {
            salvarPartida()
            onSalvo()
        }
    }

    fun onRevelarLetraClick() {
        _uiState.update { currentState ->
            if (!currentState.podeInteragir) return@update currentState

            val selecionada = currentState.letraCifradaSelecionada
                ?.takeIf { currentState.palpites[it] != currentState.respostas[it] }
            val alvo = selecionada
                ?: currentState.proximaLetraPendente(currentState.letraCifradaSelecionada)
                ?: return@update currentState
            val original = currentState.respostas[alvo] ?: return@update currentState

            val atualizado = currentState.copy(
                palpites = currentState.palpites + (alvo to original),
                dicasUsadas = currentState.dicasUsadas + 1
            )
            atualizado.copy(letraCifradaSelecionada = atualizado.proximaLetraPendente(alvo))
        }
        salvarSeDecifrado()
    }

    fun onVerOriginalClick() {
        _uiState.update { currentState ->
            if (!currentState.podeInteragir || currentState.originalRevelado) {
                return@update currentState
            }
            currentState.copy(originalRevelado = true)
        }
    }

    private fun salvarSeDecifrado() {
        if (_uiState.value.decifrado) {
            viewModelScope.launch { salvarPartida() }
        }
    }

    private suspend fun salvarPartida() {
        mutexSalvar.withLock {
            val state = _uiState.value
            val criptograma = state.criptograma ?: return
            val usuarioId = state.usuarioId ?: return
            val existente = state.partidaId?.let { repositoryPartida.buscarPorId(it) }

            // Nada foi jogado ainda, então não cria uma partida vazia.
            if (existente == null && state.palpites.isEmpty()) return

            val agora = System.currentTimeMillis()
            val concluida = state.decifrado
            val partida = Partida(
                id = existente?.id ?: 0,
                usuarioId = usuarioId,
                criptogramaId = criptograma.id,
                tempo = state.segundos,
                erros = state.erros,
                dicasUsadas = state.dicasUsadas,
                originalRevelado = state.originalRevelado,
                pontuacao = if (concluida) state.pontuacaoFinal else 0,
                concluida = concluida,
                estadoAtual = EstadoPartidaCriptograma.serializar(state.palpites),
                iniciadaEm = existente?.iniciadaEm ?: agora,
                concluidaEm = if (concluida) existente?.concluidaEm ?: agora else null
            )

            if (existente != null) {
                repositoryPartida.atualizar(partida)
            } else {
                val novoId = repositoryPartida.inserir(partida).toInt()
                _uiState.update { it.copy(partidaId = novoId) }
            }

            if (concluida && existente?.concluida != true) {
                registrarCasoResolvido(usuarioId, partida.pontuacao)
            }
        }
    }

    private suspend fun registrarCasoResolvido(usuarioId: Int, pontos: Int) {
        val usuario = repositoryUsuario.buscarPorId(usuarioId) ?: return
        repositoryUsuario.salvar(
            usuario.copy(
                casosResolvidos = usuario.casosResolvidos + 1,
                pontos = usuario.pontos + pontos
            )
        )
    }
}
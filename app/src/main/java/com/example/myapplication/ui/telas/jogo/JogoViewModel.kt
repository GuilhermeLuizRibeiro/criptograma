package com.example.myapplication.ui.telas.jogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.CelulaCriptograma
import com.example.myapplication.model.CifraCriptograma
import com.example.myapplication.model.EstadoPartidaCriptograma
import com.example.myapplication.model.PalavraCriptograma
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
    private val _uiState = MutableStateFlow(JogoUiState(carregando = true))

    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()

    private val mutexSalvar = Mutex()

    init {
        viewModelScope.launch {
            val usuario = repositoryUsuario.garantirUsuarioDefault(Papel.USUARIO).getOrElse { e ->
                registrarErro(e); return@launch
            }
            val partidas = repositoryPartida.listarTodos().first()
            val idCriptograma = criptogramaId
                ?: partidas.firstOrNull { it.usuarioId == usuario.id && !it.concluida }?.criptogramaId
            val partidaEmAndamento = partidas.firstOrNull {
                it.usuarioId == usuario.id && it.criptogramaId == idCriptograma && !it.concluida
            }
            val criptograma = idCriptograma?.let { id ->
                repositoryCriptograma.buscarPorId(id).getOrElse { e -> registrarErro(e); return@launch }
            }
            _uiState.update {
                recalcular(
                    it.copy(
                        usuarioId = usuario.id,
                        criptograma = criptograma,
                        carregando = false,
                        partidaId = partidaEmAndamento?.id,
                        segundos = partidaEmAndamento?.tempo ?: 0L,
                        erros = partidaEmAndamento?.erros ?: 0,
                        dicasUsadas = partidaEmAndamento?.dicasUsadas ?: 0,
                        palpites = partidaEmAndamento
                            ?.let { EstadoPartidaCriptograma.desserializar(it.estadoAtual) }
                            ?: emptyMap()
                    )
                )
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update {
                    if (it.podeInteragir) {
                        val s = it.segundos + 1
                        it.copy(segundos = s, tempoFormatado = formatarTempo(s))
                    } else it
                }
            }
        }
    }

    fun onCelulaClick(letraCifrada: Char) {
        _uiState.update {
            if (!it.podeInteragir) {
                it
            } else {
                recalcular(it.copy(letraCifradaSelecionada = letraCifrada))
            }
        }
    }

    fun onLetraDigitada(letra: Char) {
        _uiState.update { estado ->
            val cifrada = estado.letraCifradaSelecionada ?: return@update estado
            val original = estado.respostas[cifrada] ?: return@update estado
            if (!estado.podeInteragir || estado.palpites[cifrada] == original) return@update estado

            val errou = letra != original
            val palpites = estado.palpites + (cifrada to letra)
            val erros = if (errou && estado.palpites[cifrada] != letra) estado.erros + 1 else estado.erros
            val proxima = if (errou) cifrada
            else proximaLetraPendente(cifrada, estado.letrasCifradas, estado.respostas, palpites)

            recalcular(estado.copy(palpites = palpites, erros = erros, letraCifradaSelecionada = proxima))
        }
        salvarSeDecifrado()
    }

    fun onApagarClick() {
        _uiState.update { estado ->
            val cifrada = estado.letraCifradaSelecionada ?: return@update estado
            if (!estado.podeInteragir || estado.palpites[cifrada] == estado.respostas[cifrada]) return@update estado
            recalcular(estado.copy(palpites = estado.palpites - cifrada))
        }
    }

    fun onPausarClick(onSalvo: () -> Unit) {
        viewModelScope.launch {
            salvarPartida()
            onSalvo()
        }
    }

    fun onRevelarLetraClick() {
        _uiState.update { estado ->
            if (!estado.podeInteragir) return@update estado
            val selecionada = estado.letraCifradaSelecionada
                ?.takeIf { estado.palpites[it] != estado.respostas[it] }
            val alvo = selecionada
                ?: proximaLetraPendente(estado.letraCifradaSelecionada, estado.letrasCifradas, estado.respostas, estado.palpites)
                ?: return@update estado
            val original = estado.respostas[alvo] ?: return@update estado

            val palpites = estado.palpites + (alvo to original)
            recalcular(
                estado.copy(
                    palpites = palpites,
                    dicasUsadas = estado.dicasUsadas + 1,
                    letraCifradaSelecionada =
                        proximaLetraPendente(alvo, estado.letrasCifradas, estado.respostas, palpites)
                )
            )
        }
        salvarSeDecifrado()
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
            val existente = state.partidaId?.let {
                repositoryPartida.buscarPorId(it).getOrElse { e -> registrarErro(e); return }
            }

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
                pontuacao = if (concluida) state.pontuacaoFinal else 0,
                concluida = concluida,
                estadoAtual = EstadoPartidaCriptograma.serializar(state.palpites),
                iniciadaEm = existente?.iniciadaEm ?: agora,
                concluidaEm = if (concluida) existente?.concluidaEm ?: agora else null
            )

            val idSalvo: Int
            if (existente != null) {
                repositoryPartida.atualizar(partida).onFailure { e -> registrarErro(e); return }
                idSalvo = existente.id
            } else {
                idSalvo = repositoryPartida.inserir(partida)
                    .getOrElse { e -> registrarErro(e); return }
                    .toInt()
                _uiState.update { it.copy(partidaId = idSalvo) }
            }

            if (concluida) {
                if (existente?.concluida != true) {
                    registrarCasoResolvido(usuarioId, partida.pontuacao)
                }
                _uiState.update { it.copy(partidaConcluidaId = idSalvo) }
            }
        }
    }

    private suspend fun registrarCasoResolvido(usuarioId: Int, pontos: Int) {
        val usuario = repositoryUsuario.buscarPorId(usuarioId)
            .getOrElse { e -> registrarErro(e); return } ?: return
        repositoryUsuario.salvar(
            usuario.copy(
                casosResolvidos = usuario.casosResolvidos + 1,
                pontos = usuario.pontos + pontos
            )
        ).onFailure { e -> registrarErro(e) }
    }

    private fun registrarErro(e: Throwable) {
        _uiState.update { it.copy(carregando = false, mensagemErro = e.message) }
    }

    private fun recalcular(state: JogoUiState): JogoUiState {
        val criptograma = state.criptograma
        val palavras = if (criptograma == null) emptyList()
        else montarPalavras(criptograma, state.palpites, state.letraCifradaSelecionada)
        val celulasLetra = palavras.flatMap { it.celulas }.filter { it.ehLetra }
        val respostas = celulasLetra.associate { it.letraCifrada!! to it.letraOriginal!! }
        val decifrado = respostas.isNotEmpty() &&
                respostas.all { (cifrada, original) -> state.palpites[cifrada] == original }
        val penalidade = state.dicasUsadas * JogoUiState.CUSTO_REVELAR_LETRA

        return state.copy(
            palavras = palavras,
            respostas = respostas,
            letrasCifradas = celulasLetra.mapNotNull { it.letraCifrada }.distinct(),
            decifrado = decifrado,
            teclasUsadas = state.palpites.values.toSet(),
            tempoFormatado = formatarTempo(state.segundos),
            pontuacaoFinal = (JogoUiState.PONTOS_BASE - state.erros * JogoUiState.PENALIDADE_ERRO - penalidade).coerceAtLeast(0)
        )
    }

    private fun montarPalavras(
        criptograma: Criptograma,
        palpites: Map<Char, Char>,
        selecionada: Char?
    ): List<PalavraCriptograma> {
        val mapa = CifraCriptograma.gerarMapa(criptograma.id)
        return criptograma.frase
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .map { palavra ->
                PalavraCriptograma(
                    palavra.map { caractere ->
                        val original = CifraCriptograma.normalizar(caractere)
                        val cifrada = mapa[original]
                        if (cifrada == null) CelulaCriptograma(caractere = caractere)
                        else CelulaCriptograma(
                            caractere = caractere,
                            letraOriginal = original,
                            letraCifrada = cifrada,
                            palpite = palpites[cifrada],
                            destacada = cifrada == selecionada
                        )
                    }
                )
            }
    }

    private fun proximaLetraPendente(
        atual: Char?,
        letras: List<Char>,
        respostas: Map<Char, Char>,
        palpites: Map<Char, Char>
    ): Char? {
        if (letras.isEmpty()) return null
        val inicio = (atual?.let { letras.indexOf(it) } ?: -1) + 1
        return (letras.drop(inicio) + letras.take(inicio))
            .firstOrNull { palpites[it] != respostas[it] }
    }

    private fun formatarTempo(segundos: Long) = "%02d:%02d".format(segundos / 60, segundos % 60)
}
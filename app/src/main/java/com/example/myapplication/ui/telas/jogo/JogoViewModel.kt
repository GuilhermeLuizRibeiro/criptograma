package com.example.myapplication.ui.telas.jogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    init {
        viewModelScope.launch {
            val usuario = repositoryUsuario.garantirUsuarioDefault(Papel.USUARIO)
            _uiState.update { currentState ->
                currentState.copy(usuarioId = usuario.id, carregando = false)
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

    fun onPausarClick() {
        _uiState.update { currentState ->
            if (currentState.decifrado) return@update currentState
            currentState.copy(pausado = !currentState.pausado)
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
    }

    fun onVerOriginalClick() {
        _uiState.update { currentState ->
            if (!currentState.podeInteragir || currentState.originalRevelado) {
                return@update currentState
            }
            currentState.copy(originalRevelado = true)
        }
    }
}
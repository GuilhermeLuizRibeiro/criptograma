package com.example.myapplication.ui.telas.casos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CasosViewModel(
    private val repositoryUsuario: UsuarioRepository,
    private val repositoryPartida: PartidaRepository,
    private val repositoryCriptograma: CriptogramaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CasosUiState(carregando = true))

    val uiState: StateFlow<CasosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryUsuario.garantirUsuarioDefault(Papel.USUARIO)
                .onSuccess { usuario ->
                    _uiState.update { currentState ->
                        currentState.copy(usuarioId = usuario.id, carregando = false)
                    }
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
    }

    fun onDificuldadeSelecionada(dificuldade: Dificuldade?) {
        _uiState.update { it.copy(dificuldadeSelecionada = dificuldade) }
    }

    fun onCategoriaSelecionada(categoria: String?) {
        _uiState.update { it.copy(categoriaSelecionada = categoria) }
    }
}
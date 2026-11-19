package com.example.myapplication.ui.telas.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InicioViewModel(
    private val repositoryUsuario: UsuarioRepository,
    private val repositoryPartida: PartidaRepository,
    private val repositoryCriptograma: CriptogramaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState(carregando = true))

    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryUsuario.garantirUsuarioDefault(Papel.USUARIO)
                .onSuccess { usuario ->
                    _uiState.update { recalcular(it.copy(usuarioId = usuario.id, carregando = false)) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, mensagemErro = erro.message) }
                }
        }

        viewModelScope.launch {
            repositoryUsuario.listarTodos().collect { usuarios ->
                _uiState.update { currentState ->
                    currentState.copy(usuarios = usuarios)
                }
            }
        }

        viewModelScope.launch {
            repositoryPartida.listarTodos().collect { partidas ->
                _uiState.update {
                    recalcular(it.copy(partidas = partidas))
                }
            }
        }

        viewModelScope.launch {
            repositoryCriptograma.listarTodos().collect { criptogramas ->
                _uiState.update {
                    recalcular(it.copy(criptogramas = criptogramas))
                }
            }
        }
    }

    private fun recalcular(state: InicioUiState): InicioUiState {
        val doUsuario = state.partidas.filter { it.usuarioId == state.usuarioId }
        return state.copy(
            partidasDoUsuario = doUsuario,
            historico = doUsuario
                .filter { it.concluida }
                .sortedByDescending { it.concluidaEm ?: it.iniciadaEm }
        )
    }
}
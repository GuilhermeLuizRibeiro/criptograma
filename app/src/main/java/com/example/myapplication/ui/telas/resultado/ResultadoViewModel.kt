package com.example.myapplication.ui.telas.resultado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultadoViewModel(
    private val repositoryUsuario: UsuarioRepository,
    private val repositoryPartida: PartidaRepository,
    private val repositoryCriptograma: CriptogramaRepository,
    partidaId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultadoUiState(carregando = true))

    val uiState: StateFlow<ResultadoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val partida = repositoryPartida.buscarPorId(partidaId)
            val criptograma = partida?.criptogramaId?.let { repositoryCriptograma.buscarPorId(it) }
            val usuario = partida?.let { repositoryUsuario.buscarPorId(it.usuarioId) }
            _uiState.update { currentState ->
                currentState.copy(
                    partida = partida,
                    criptograma = criptograma,
                    usuario = usuario,
                    carregando = false
                )
            }
        }
    }
}
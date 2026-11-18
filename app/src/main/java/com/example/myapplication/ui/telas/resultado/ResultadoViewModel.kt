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
                .getOrElse { e -> mostrarErro(e); return@launch }
            val criptograma = partida?.criptogramaId?.let {
                repositoryCriptograma.buscarPorId(it)
                    .getOrElse { e -> mostrarErro(e); return@launch }
            }
            val usuario = partida?.let {
                repositoryUsuario.buscarPorId(it.usuarioId)
                    .getOrElse { e -> mostrarErro(e); return@launch }
            }
            _uiState.update {
                it.copy(partida = partida, criptograma = criptograma, usuario = usuario, carregando = false)
            }
        }
    }

    private fun mostrarErro(e: Throwable) {
        _uiState.update { it.copy(carregando = false, mensagemErro = e.message) }
    }
}
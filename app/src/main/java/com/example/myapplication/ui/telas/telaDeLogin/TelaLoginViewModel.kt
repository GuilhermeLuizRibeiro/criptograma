package com.example.myapplication.ui.telas.telaDeLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaLoginViewModel(
    private val usuarioRepository: UsuarioRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TelaLoginUiState())
    val uiState: StateFlow<TelaLoginUiState> = _uiState.asStateFlow()

    fun entrarComoAdmin(onSucesso: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            usuarioRepository.garantirUsuarioDefault(Papel.ADMIN)
                .onSuccess { usuario ->
                    _uiState.update { it.copy(usuarioLogado = usuario, carregando = false) }
                    onSucesso()
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false) }
                    _uiState.update { it.copy(mensagemErro = erro.message) }
                }
        }
    }

    fun entrarComoJogador(onSucesso: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            usuarioRepository.garantirUsuarioDefault(Papel.USUARIO)
                .onSuccess { usuario ->
                    _uiState.update { it.copy(usuarioLogado = usuario, carregando = false) }
                    onSucesso()
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false) }
                    _uiState.update { it.copy(mensagemErro = erro.message) }
                }
        }
    }
}

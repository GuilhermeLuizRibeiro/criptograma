package com.example.myapplication.ui.telas.telaDePerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaPerfilViewModel(
    private val usuarioRepository: UsuarioRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TelaPerfilUiState())
    val uiState: StateFlow<TelaPerfilUiState> = _uiState.asStateFlow()

    fun carregarUsuario(papel: Papel) {
        viewModelScope.launch {
            val user = usuarioRepository.garantirUsuarioDefault(papel)
            _uiState.update {
                it.copy(usuario = user)
            }
        }
    }

    fun onItemNavegacaoChange(novoItem: String) {
        _uiState.update { it.copy(itemNavegacaoSelecionado = novoItem) }
    }
}

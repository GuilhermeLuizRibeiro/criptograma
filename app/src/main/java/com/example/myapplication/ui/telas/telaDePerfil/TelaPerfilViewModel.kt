package com.example.myapplication.ui.telas.telaDePerfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaPerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(AppDatabase.getDatabase(application).usuarioDAO())

    private val _uiState = MutableStateFlow(TelaPerfilUiState())
    val uiState: StateFlow<TelaPerfilUiState> = _uiState.asStateFlow()

    init {
        carregarUsuario(Papel.ADMIN)
    }

    fun carregarUsuario(papel: Papel = Papel.ADMIN) {
        viewModelScope.launch {
            val user = repository.garantirUsuarioDefault(papel)
            _uiState.update {
                it.copy(usuario = user)
            }
        }
    }

    fun onItemNavegacaoChange(novoItem: String) {
        _uiState.update { it.copy(itemNavegacaoSelecionado = novoItem) }
    }
}

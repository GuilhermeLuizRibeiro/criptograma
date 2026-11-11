package com.example.myapplication.ui.telas.telaDeLogin

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

class TelaLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(AppDatabase.getDatabase(application).usuarioDAO())

    private val _uiState = MutableStateFlow(TelaLoginUiState())
    val uiState: StateFlow<TelaLoginUiState> = _uiState.asStateFlow()

    fun entrarComoAdmin(onSucesso: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            val usuario = repository.garantirUsuarioDefault(Papel.ADMIN)
            _uiState.update { it.copy(usuarioLogado = usuario, carregando = false) }
            onSucesso()
        }
    }

    fun entrarComoJogador(onSucesso: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            val usuario = repository.garantirUsuarioDefault(Papel.USUARIO)
            _uiState.update { it.copy(usuarioLogado = usuario, carregando = false) }
            onSucesso()
        }
    }
}

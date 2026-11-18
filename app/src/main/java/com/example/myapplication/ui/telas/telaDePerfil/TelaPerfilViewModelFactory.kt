package com.example.myapplication.ui.telas.telaDePerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.UsuarioRepository

class TelaPerfilViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TelaPerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TelaPerfilViewModel(usuarioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

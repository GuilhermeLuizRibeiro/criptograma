package com.example.myapplication.ui.telas.telaDeLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.UsuarioRepository

class TelaLoginViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TelaLoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TelaLoginViewModel(usuarioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.myapplication.ui.telas.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository

class InicioViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val partidaRepository: PartidaRepository,
    private val criptogramaRepository: CriptogramaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InicioViewModel(usuarioRepository, partidaRepository, criptogramaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
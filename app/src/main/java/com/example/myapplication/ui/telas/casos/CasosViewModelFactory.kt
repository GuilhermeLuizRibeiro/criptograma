package com.example.myapplication.ui.telas.casos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository

class CasosViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val partidaRepository: PartidaRepository,
    private val criptogramaRepository: CriptogramaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CasosViewModel(usuarioRepository, partidaRepository, criptogramaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
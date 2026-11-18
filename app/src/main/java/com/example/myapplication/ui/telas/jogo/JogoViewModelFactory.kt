package com.example.myapplication.ui.telas.jogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository

class JogoViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val partidaRepository: PartidaRepository,
    private val criptogramaRepository: CriptogramaRepository,
    private val criptogramaId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JogoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JogoViewModel(
                usuarioRepository,
                partidaRepository,
                criptogramaRepository,
                criptogramaId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
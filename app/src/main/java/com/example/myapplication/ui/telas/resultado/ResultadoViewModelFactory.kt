package com.example.myapplication.ui.telas.resultado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository

class ResultadoViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val partidaRepository: PartidaRepository,
    private val criptogramaRepository: CriptogramaRepository,
    private val partidaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultadoViewModel(
                usuarioRepository,
                partidaRepository,
                criptogramaRepository,
                partidaId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
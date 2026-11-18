package com.example.myapplication.ui.telas.telaCriarCriptograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.EstoicaRepository

class TelaCriaCriptogramasAdminViewModelFactory(
    private val criptogramaRepository: CriptogramaRepository,
    private val estoicaRepository: EstoicaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TelaCriaCriptogramasAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TelaCriaCriptogramasAdminViewModel(criptogramaRepository, estoicaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

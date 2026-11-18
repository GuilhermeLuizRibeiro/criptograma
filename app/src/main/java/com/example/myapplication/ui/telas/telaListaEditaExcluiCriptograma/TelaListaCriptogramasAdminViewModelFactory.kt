package com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CriptogramaRepository

class TelaListaCriptogramasAdminViewModelFactory(
    private val criptogramaRepository: CriptogramaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TelaListaCriptogramasAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TelaListaCriptogramasAdminViewModel(criptogramaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

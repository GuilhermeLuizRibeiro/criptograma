package com.example.myapplication.ui.telas.telaDoAdmin

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TelaCriaCriptogramasAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TelaCriaCriptogramasAdminUiState())
    val uiState: StateFlow<TelaCriaCriptogramasAdminUiState> = _uiState.asStateFlow()

    fun onFraseTraducaoChange(novaFrase: String) {
        _uiState.update { it.copy(fraseTraducao = novaFrase) }
    }

    fun onFraseOriginalChange(novaFrase: String) {
        _uiState.update { it.copy(fraseOriginal = novaFrase) }
    }

    fun onAutorChange(novoAutor: String) {
        _uiState.update { it.copy(autor = novoAutor) }
    }

    fun onCategoriaSelecionadaChange(novaCategoria: String) {
        _uiState.update { it.copy(categoriaSelecionada = novaCategoria, menuCategoriaExpandido = false) }
    }

    fun onDificuldadeSelecionadaChange(novaDificuldade: Dificuldade) {
        _uiState.update { it.copy(dificuldadeSelecionada = novaDificuldade) }
    }

    fun onMenuCategoriaExpandidoChange(expandido: Boolean) {
        _uiState.update { it.copy(menuCategoriaExpandido = expandido) }
    }

    fun onClickSalvar(): Boolean {
        _uiState.update { it.copy(tentouSalvar = true) }
        return !_uiState.value.erros.temErros
    }

    fun criarCriptograma(): Criptograma {
        val state = _uiState.value
        return Criptograma(
            fraseOriginal = state.fraseOriginal,
            fraseTraducao = state.fraseTraducao,
            autor = state.autor,
            categoria = state.categoriaSelecionada,
            dificuldade = state.dificuldadeSelecionada,
            idExterno = "API"
        )
    }
}

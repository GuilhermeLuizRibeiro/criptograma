package com.example.myapplication.ui.telas.telaCriarCriptograma

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.model.enums.Dificuldade
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaCriaCriptogramasAdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CriptogramaRepository(AppDatabase.getDatabase(application).criptogramaDAO())

    private val _uiState = MutableStateFlow(TelaCriaCriptogramasAdminUiState())
    val uiState: StateFlow<TelaCriaCriptogramasAdminUiState> = _uiState.asStateFlow()

    fun carregarCriptogramaPorId(id: Int) {
        if (id <= 0) return
        viewModelScope.launch {
            val cripto = repository.buscarPorId(id)
            if (cripto != null) {
                _uiState.update {
                    it.copy(
                        criptogramaEmEdicao = cripto,
                        fraseTraducao = cripto.fraseTraducao,
                        fraseOriginal = cripto.fraseOriginal ?: "",
                        autor = cripto.autor,
                        categoriaSelecionada = cripto.categoria,
                        dificuldadeSelecionada = cripto.dificuldade,
                        origemSelecionada = if (cripto.idExterno?.startsWith("API") == true) "API de citações" else "Manual"
                    )
                }
            }
        }
    }

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

    fun onOrigemSelecionadaChange(novaOrigem: String) {
        _uiState.update { it.copy(origemSelecionada = novaOrigem) }
    }

    fun onMenuCategoriaExpandidoChange(expandido: Boolean) {
        _uiState.update { it.copy(menuCategoriaExpandido = expandido) }
    }

    fun salvarCriptograma(onSucesso: (Criptograma) -> Unit) {
        _uiState.update { it.copy(tentouSalvar = true) }
        if (_uiState.value.erros.temErros) return

        viewModelScope.launch {
            val cripto = criarCriptograma()
            repository.salvar(cripto)
            onSucesso(cripto)
        }
    }

    private fun criarCriptograma(): Criptograma {
        val state = _uiState.value
        val prefixo = if (state.origemSelecionada == "Manual") "MANUAL" else "API"

        val idExternoFinal = if (state.criptogramaEmEdicao != null) {
            state.criptogramaEmEdicao.idExterno ?: "${prefixo}_${UUID.randomUUID().toString().take(8)}"
        } else {
            "${prefixo}_${UUID.randomUUID().toString().take(8)}"
        }

        return Criptograma(
            id = state.criptogramaEmEdicao?.id ?: 0,
            fraseOriginal = state.fraseOriginal,
            fraseTraducao = state.fraseTraducao,
            autor = state.autor,
            categoria = state.categoriaSelecionada,
            dificuldade = state.dificuldadeSelecionada,
            idExterno = idExternoFinal
        )
    }
}

package com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.model.enums.Dificuldade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TelaListaCriptogramasAdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CriptogramaRepository(AppDatabase.getDatabase(application).criptogramaDAO())

    private val _uiState = MutableStateFlow(TelaListaCriptogramasAdminUiState(itemNavegacaoSelecionado = "Painel"))
    val uiState: StateFlow<TelaListaCriptogramasAdminUiState> = _uiState.asStateFlow()

    private var jaPopulouIniciais = false

    init {
        observarCriptogramas()
    }

    private fun observarCriptogramas() {
        viewModelScope.launch {
            repository.listarTodos().collect { lista ->
                if (lista.isEmpty() && !jaPopulouIniciais) {
                    jaPopulouIniciais = true
                    popularDadosIniciaisSeVazio()
                } else {
                    _uiState.update { it.copy(listaCriptogramas = lista) }
                }
            }
        }
    }

    private suspend fun popularDadosIniciaisSeVazio() {
        val iniciais = listOf(
            Criptograma(
                fraseOriginal = "The secret of getting ahead is getting started.",
                fraseTraducao = "O segredo para progredir é começar.",
                autor = "Mark Twain",
                categoria = "Citações",
                dificuldade = Dificuldade.DIFICIL,
                idExterno = "API_0007"
            ),
            Criptograma(
                fraseOriginal = "He who tells a tale adds a point.",
                fraseTraducao = "Quem conta um conto aumenta um ponto.",
                autor = "Provérbio",
                categoria = "Citações",
                dificuldade = Dificuldade.MEDIO,
                idExterno = "MANUAL_0008"
            ),
            Criptograma(
                fraseOriginal = "Imagination is more important than knowledge.",
                fraseTraducao = "A imaginação é mais importante que o conhecimento.",
                autor = "Albert Einstein",
                categoria = "Citações",
                dificuldade = Dificuldade.FACIL,
                idExterno = "API_0006"
            )
        )
        iniciais.forEach { repository.salvar(it) }
    }

    fun onBuscaChange(novaBusca: String) {
        _uiState.update { it.copy(busca = novaBusca) }
    }

    fun onFiltroSelecionadoChange(novoFiltro: String) {
        _uiState.update { it.copy(filtroSelecionado = novoFiltro) }
    }

    fun onItemNavegacaoChange(novoItem: String) {
        _uiState.update { it.copy(itemNavegacaoSelecionado = novoItem) }
    }

    fun excluirCriptograma(criptograma: Criptograma) {
        viewModelScope.launch {
            repository.excluir(criptograma)
        }
    }
}

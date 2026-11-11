package com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TelaListaCriptogramasAdminViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        TelaListaCriptogramasAdminUiState(
            itemNavegacaoSelecionado = "Painel",
            listaCriptogramas = listOf(
                Criptograma(
                    id = 7,
                    fraseTraducao = "O segredo para progredir é começar.",
                    autor = "Mark Twain",
                    categoria = "Citações",
                    dificuldade = Dificuldade.DIFICIL,
                    idExterno = "API"
                ),
                Criptograma(
                    id = 8,
                    fraseTraducao = "Quem conta um conto aumenta um ponto.",
                    autor = "Provérbio",
                    categoria = "Citações",
                    dificuldade = Dificuldade.MEDIO,
                    idExterno = "Manual"
                ),
                Criptograma(
                    id = 6,
                    fraseTraducao = "A imaginação é mais importante que o conhecimento.",
                    autor = "Albert Einstein",
                    categoria = "Citações",
                    dificuldade = Dificuldade.FACIL,
                    idExterno = "API"
                )
            )
        )
    )
    val uiState: StateFlow<TelaListaCriptogramasAdminUiState> = _uiState.asStateFlow()

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
        _uiState.update { state ->
            state.copy(listaCriptogramas = state.listaCriptogramas.filter { it.id != criptograma.id })
        }
    }
}

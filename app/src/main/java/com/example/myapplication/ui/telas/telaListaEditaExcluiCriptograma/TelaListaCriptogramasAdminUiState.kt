package com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade

data class TelaListaCriptogramasAdminUiState(
    val busca: String = "",
    val filtroSelecionado: String = "Todos",
    val itemNavegacaoSelecionado: String = "Painel",
    val listaCriptogramas: List<Criptograma> = emptyList(),
) {
    val listaFiltrada: List<Criptograma>
        get() = listaCriptogramas.filter { item ->
            val bateBusca = busca.isBlank() ||
                    item.fraseTraducao.contains(busca, ignoreCase = true) ||
                    item.autor.contains(busca, ignoreCase = true)

            val bateDificuldade = when (filtroSelecionado) {
                "Fácil" -> item.dificuldade == Dificuldade.FACIL
                "Médio" -> item.dificuldade == Dificuldade.MEDIO
                "Difícil" -> item.dificuldade == Dificuldade.DIFICIL
                else -> true
            }

            bateBusca && bateDificuldade
        }

    val quantidadeCasos: Int
        get() = listaFiltrada.size
}

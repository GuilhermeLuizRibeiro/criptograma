package com.example.myapplication.ui.telas.telaCriarCriptograma

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.ErrosFormCriptograma
import com.example.myapplication.model.enums.Dificuldade

data class TelaCriaCriptogramasAdminUiState(
    val frase: String = "",
    val autor: String = "",
    val categoriaSelecionada: String = "Citações",
    val dificuldadeSelecionada: Dificuldade = Dificuldade.FACIL,
    val origemSelecionada: String = "Manual",
    val menuCategoriaExpandido: Boolean = false,
    val categoriasDisponiveis: List<String> = listOf("Citações", "Provérbios", "Cinema", "Literatura", "História"),
    val origensDisponiveis: List<String> = listOf("Manual", "API de citações"),
    val criptogramaEmEdicao: Criptograma? = null,
    val tentouSalvar: Boolean = false,
) {
    val editando: Boolean
        get() = criptogramaEmEdicao != null

    val erros: ErrosFormCriptograma
        get() = ErrosFormCriptograma(
            frase = if (frase.isBlank()) "Obrigatória para salvar." else null
        )
}

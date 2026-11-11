package com.example.myapplication.ui.telas.telaCriarCriptograma

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.ErrosFormCriptograma
import com.example.myapplication.model.enums.Dificuldade

data class TelaCriaCriptogramasAdminUiState(
    val fraseTraducao: String = "",
    val fraseOriginal: String = "",
    val autor: String = "",
    val categoriaSelecionada: String = "Citações",
    val dificuldadeSelecionada: Dificuldade = Dificuldade.FACIL,
    val menuCategoriaExpandido: Boolean = false,
    val categoriasDisponiveis: List<String> = listOf("Citações", "Provérbios", "Cinema", "Literatura", "História"),
    val criptogramaEmEdicao: Criptograma? = null,
    val tentouSalvar: Boolean = false,
) {
    val editando: Boolean
        get() = criptogramaEmEdicao != null

    val erros: ErrosFormCriptograma
        get() = ErrosFormCriptograma(
            fraseTraducao = if (fraseTraducao.isBlank()) "Obrigatória para salvar." else null
        )
}

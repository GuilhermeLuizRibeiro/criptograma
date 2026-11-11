package com.example.myapplication.ui.telas.telaCriarCriptograma

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.ErrosFormCriptograma
import com.example.myapplication.model.enums.Dificuldade

data class TelaCriaCriptogramasAdminUiState(
    val fraseTraducao: String = "",
    val fraseOriginal: String = "The secret of getting ahead is getting started.",
    val autor: String = "Mark Twain",
    val categoriaSelecionada: String = "Citações",
    val dificuldadeSelecionada: Dificuldade = Dificuldade.DIFICIL,
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

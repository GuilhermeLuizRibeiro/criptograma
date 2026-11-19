package com.example.myapplication.ui.telas.jogo

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.PalavraCriptograma
import com.example.myapplication.model.enums.StatusCaso

data class JogoUiState(
    val usuarioId: Int? = null,
    val partidaId: Int? = null,
    val partidaConcluidaId: Int? = null,
    val criptograma: Criptograma? = null,
    val palpites: Map<Char, Char> = emptyMap(),
    val letraCifradaSelecionada: Char? = null,
    val segundos: Long = 0L,
    val erros: Int = 0,
    val dicasUsadas: Int = 0,
    val pausado: Boolean = false,
    val carregando: Boolean = false,
    val mensagemErro: String? = null,
    val respostas: Map<Char, Char> = emptyMap(),
    val letrasCifradas: List<Char> = emptyList(),
    val palavras: List<PalavraCriptograma> = emptyList(),
    val decifrado: Boolean = false,
    val teclasUsadas: Set<Char> = emptySet(),
    val tempoFormatado: String = "00:00",
    val pontuacaoFinal: Int = 0,
) {
    companion object {
        const val CUSTO_REVELAR_LETRA = 10
        const val PONTOS_BASE = 100
        const val PENALIDADE_ERRO = 5
    }

    val linhasTeclado: List<String> get() = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")
    val frase: String get() = criptograma?.frase ?: ""
    val autor: String get() = criptograma?.autor ?: ""
    val autorRevelado: Boolean get() = decifrado
    val statusCaso: StatusCaso get() = if (decifrado) StatusCaso.DECIFRADO else StatusCaso.EM_ANDAMENTO
    val podeInteragir: Boolean get() = criptograma != null && !pausado && !decifrado
}
package com.example.myapplication.ui.telas.resultado

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.local.entity.Usuario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ResultadoUiState(
    val partida: Partida? = null,
    val criptograma: Criptograma? = null,
    val usuario: Usuario? = null,
    val carregando: Boolean = false
) {
    val carregado: Boolean
        get() = partida != null && criptograma != null

    val tempoFormatado: String
        get() {
            val segundos = partida?.tempo ?: 0L
            return "%02d:%02d".format(segundos / 60, segundos % 60)
        }

    val erros: Int
        get() = partida?.erros ?: 0

    val totalDicas: Int
        get() = (partida?.dicasUsadas ?: 0) + if (partida?.originalRevelado == true) 1 else 0

    val pontuacao: Int
        get() = partida?.pontuacao ?: 0

    val pontuacaoFormatada: String
        get() = "%,d".format(pontuacao)

    val dataFormatada: String
        get() {
            val data = partida?.concluidaEm ?: partida?.iniciadaEm ?: return ""
            return SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(data))
        }

    val nomeUsuario: String
        get() = usuario?.nome ?: "Jogador"

    val fraseEntreAspas: String
        get() = "\u201C${criptograma?.frase ?: ""}\u201D"

    val autor: String
        get() = criptograma?.autor ?: ""
}
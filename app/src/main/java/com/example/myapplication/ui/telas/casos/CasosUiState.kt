package com.example.myapplication.ui.telas.casos

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.model.enums.StatusCaso

data class CasosUiState(
    val usuarioId: Int? = null,
    val criptogramas: List<Criptograma> = emptyList(),
    val partidas: List<Partida> = emptyList(),
    val dificuldadeSelecionada: Dificuldade? = null,
    val categoriaSelecionada: String? = null,
    val itemNavegacaoSelecionado: String = "Casos",
    val carregando: Boolean = false
) {
    fun partidasDoCriptograma(criptograma: Criptograma): List<Partida> {
        return partidas.filter {
            it.usuarioId == usuarioId && it.criptogramaId == criptograma.id
        }
    }

    fun statusDoCaso(criptograma: Criptograma): StatusCaso {
        val partidasDoCaso = partidasDoCriptograma(criptograma)
        return when {
            partidasDoCaso.any { it.concluida } -> StatusCaso.DECIFRADO
            partidasDoCaso.isNotEmpty() -> StatusCaso.EM_ANDAMENTO
            else -> StatusCaso.NOVO
        }
    }

    fun pontosDoCaso(criptograma: Criptograma): Int {
        return partidasDoCriptograma(criptograma)
            .filter { it.concluida }
            .maxOfOrNull { it.pontuacao } ?: 0
    }

    fun tempoFormatadoDoCaso(criptograma: Criptograma): String {
        val partidaEmAndamento = partidasDoCriptograma(criptograma).firstOrNull { !it.concluida }
        val segundos = partidaEmAndamento?.tempo ?: 0L
        return "%02d:%02d".format(segundos / 60, segundos % 60)
    }

    fun totalLetras(criptograma: Criptograma): Int {
        return criptograma.frase.count { it.isLetter() }
    }

    fun totalPalavras(criptograma: Criptograma): Int {
        return criptograma.frase.split(Regex("\\s+")).count { it.isNotBlank() }
    }

    val casosPendentes: List<Criptograma>
        get() = criptogramas.filter { statusDoCaso(it) != StatusCaso.DECIFRADO }

    val categorias: List<String>
        get() = casosPendentes.map { it.categoria }.distinct().sorted()

    val criptogramasFiltrados: List<Criptograma>
        get() = casosPendentes.filter { criptograma ->
            val bateDificuldade = dificuldadeSelecionada == null ||
                    criptograma.dificuldade == dificuldadeSelecionada
            val bateCategoria = categoriaSelecionada == null ||
                    criptograma.categoria == categoriaSelecionada
            bateDificuldade && bateCategoria
        }

    val quantidadeCasos: Int
        get() = criptogramasFiltrados.size
}
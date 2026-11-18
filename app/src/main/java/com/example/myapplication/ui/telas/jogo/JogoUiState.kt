package com.example.myapplication.ui.telas.jogo

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.model.CelulaCriptograma
import com.example.myapplication.model.CifraCriptograma
import com.example.myapplication.model.PalavraCriptograma
import com.example.myapplication.model.enums.StatusCaso

data class JogoUiState(
    val criptogramaId: Int? = null,
    val usuarioId: Int? = null,
    val partidaId: Int? = null,
    val partidas: List<Partida> = emptyList(),
    val criptogramas: List<Criptograma> = emptyList(),
    val palpites: Map<Char, Char> = emptyMap(),
    val letraCifradaSelecionada: Char? = null,
    val segundos: Long = 0L,
    val erros: Int = 0,
    val dicasUsadas: Int = 0,
    val originalRevelado: Boolean = false,
    val pausado: Boolean = false,
    val carregando: Boolean = false
) {
    companion object {
        const val CUSTO_REVELAR_LETRA = 10
        const val CUSTO_VER_ORIGINAL = 25
        const val PONTOS_BASE = 100
        const val PENALIDADE_ERRO = 5
    }

    val linhasTeclado: List<String>
        get() = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")

    val idCriptogramaAtual: Int?
        get() = criptogramaId
            ?: partidas.firstOrNull { it.usuarioId == usuarioId && !it.concluida }?.criptogramaId

    val criptograma: Criptograma?
        get() = criptogramas.find { it.id == idCriptogramaAtual }

    val palavras: List<PalavraCriptograma>
        get() {
            val criptograma = criptograma ?: return emptyList()
            val mapa = CifraCriptograma.gerarMapa(criptograma.id)
            return criptograma.frase
                .split(Regex("\\s+"))
                .filter { it.isNotBlank() }
                .map { palavra ->
                    PalavraCriptograma(
                        palavra.map { caractere ->
                            val original = CifraCriptograma.normalizar(caractere)
                            val cifrada = mapa[original]
                            if (cifrada == null) {
                                CelulaCriptograma(caractere = caractere)
                            } else {
                                CelulaCriptograma(
                                    caractere = caractere,
                                    letraOriginal = original,
                                    letraCifrada = cifrada,
                                    palpite = palpites[cifrada],
                                    destacada = cifrada == letraCifradaSelecionada
                                )
                            }
                        }
                    )
                }
        }

    val letrasCifradas: List<Char>
        get() = palavras
            .flatMap { it.celulas }
            .mapNotNull { it.letraCifrada }
            .distinct()

    val respostas: Map<Char, Char>
        get() = palavras
            .flatMap { it.celulas }
            .filter { it.ehLetra }
            .associate { it.letraCifrada!! to it.letraOriginal!! }

    val decifrado: Boolean
        get() {
            val respostas = respostas
            return respostas.isNotEmpty() && respostas.all { (cifrada, original) ->
                palpites[cifrada] == original
            }
        }

    val statusCaso: StatusCaso
        get() = if (decifrado) StatusCaso.DECIFRADO else StatusCaso.EM_ANDAMENTO

    val podeInteragir: Boolean
        get() = criptograma != null && !pausado && !decifrado

    val teclasUsadas: Set<Char>
        get() = palpites.values.toSet()

    val tempoFormatado: String
        get() = "%02d:%02d".format(segundos / 60, segundos % 60)

    val frase: String
        get() = criptograma?.frase ?: ""

    val autor: String
        get() = criptograma?.autor ?: ""

    val autorRevelado: Boolean
        get() = decifrado

    val penalidade: Int
        get() = dicasUsadas * CUSTO_REVELAR_LETRA +
                if (originalRevelado) CUSTO_VER_ORIGINAL else 0

    val pontuacaoFinal: Int
        get() = (PONTOS_BASE - erros * PENALIDADE_ERRO - penalidade).coerceAtLeast(0)

    fun proximaLetraPendente(atual: Char?): Char? {
        val letras = letrasCifradas
        if (letras.isEmpty()) return null
        val respostas = respostas
        val inicio = (atual?.let { letras.indexOf(it) } ?: -1) + 1
        return (letras.drop(inicio) + letras.take(inicio))
            .firstOrNull { palpites[it] != respostas[it] }
    }
}
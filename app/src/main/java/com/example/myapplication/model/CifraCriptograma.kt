package com.example.myapplication.model

import java.text.Normalizer
import kotlin.random.Random

object CifraCriptograma {
    private const val ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun normalizar(caractere: Char): Char {
        val base = Normalizer.normalize(caractere.toString(), Normalizer.Form.NFD).first()
        return base.uppercaseChar()
    }

    fun gerarMapa(semente: Int): Map<Char, Char> {
        val aleatorio = Random(semente)
        val letras = ALFABETO.toList()
        var embaralhadas: List<Char>
        do {
            embaralhadas = letras.shuffled(aleatorio)
        } while (letras.indices.any { letras[it] == embaralhadas[it] })
        return letras.zip(embaralhadas).toMap()
    }
}
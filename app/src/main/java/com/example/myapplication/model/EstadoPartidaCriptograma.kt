package com.example.myapplication.model

object EstadoPartidaCriptograma {
    private const val SEPARADOR = ","

    fun serializar(palpites: Map<Char, Char>): String {
        return palpites.entries.joinToString(SEPARADOR) { "${it.key}${it.value}" }
    }

    fun desserializar(estado: String): Map<Char, Char> {
        return estado
            .split(SEPARADOR)
            .filter { it.length == 2 }
            .associate { it[0] to it[1] }
    }
}
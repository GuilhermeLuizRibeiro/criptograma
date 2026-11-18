package com.example.myapplication.model

data class CelulaCriptograma(
    val caractere: Char,
    val letraOriginal: Char? = null,
    val letraCifrada: Char? = null,
    val palpite: Char? = null,
    val destacada: Boolean = false
) {
    val ehLetra: Boolean
        get() = letraCifrada != null

    val correta: Boolean
        get() = ehLetra && palpite == letraOriginal

    val errada: Boolean
        get() = ehLetra && palpite != null && palpite != letraOriginal
}
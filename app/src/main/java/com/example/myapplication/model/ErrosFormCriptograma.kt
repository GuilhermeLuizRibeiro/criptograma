package com.example.myapplication.model

data class ErrosFormCriptograma(
    val frase: String? = null,
    val autor: String? = null,
) {
    val temErros: Boolean
        get() = frase != null || autor != null
}

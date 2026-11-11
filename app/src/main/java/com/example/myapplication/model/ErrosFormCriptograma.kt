package com.example.myapplication.model

data class ErrosFormCriptograma(
    val fraseTraducao: String? = null,
    val fraseOriginal: String? = null,
    val autor: String? = null,
) {
    val temErros: Boolean
        get() = fraseTraducao != null || fraseOriginal != null || autor != null
}

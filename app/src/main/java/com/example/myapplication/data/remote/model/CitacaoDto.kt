package com.example.myapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class CitacaoDto(
    @SerializedName("author", alternate = ["autor", "writer", "stoic"])
    val autor: String? = null,

    @SerializedName("text", alternate = ["quote", "texto", "frase", "content", "body"])
    val texto: String? = null,

    @SerializedName("id", alternate = ["_id", "code"])
    val id: String? = null,
)

package com.example.myapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class EstoicaResponse(
    @SerializedName("total", alternate = ["count", "totalResults"])
    val total: Int? = 0,

    @SerializedName("quotes", alternate = ["citacoes", "data", "results", "phrases"])
    val quotes: List<CitacaoDto>? = emptyList(),
)

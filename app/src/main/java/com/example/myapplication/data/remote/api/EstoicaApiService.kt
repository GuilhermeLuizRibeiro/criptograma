package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.model.CitacaoDto
import retrofit2.http.GET

interface EstoicaApiService {
    @GET("api/quotes")
    suspend fun buscarQuotes(): List<CitacaoDto>
}

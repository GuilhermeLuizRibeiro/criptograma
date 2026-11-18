package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.api.EstoicaApiService
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.model.CitacaoDto

class EstoicaRepository(
    private val apiService: EstoicaApiService = RetrofitClient.estoicaApiService,
) {
    suspend fun buscarCitacoes(termo: String): Result<List<CitacaoDto>> {
        val termoLimpo = termo.trim().lowercase()

        return try {
            val listaRemota = apiService.buscarQuotes()

            val filtradas = listaRemota.filter { item ->
                !item.texto.isNullOrBlank() &&
                        (item.texto.lowercase().contains(termoLimpo) || item.autor?.lowercase()?.contains(termoLimpo) == true)
            }

            Result.success(filtradas)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

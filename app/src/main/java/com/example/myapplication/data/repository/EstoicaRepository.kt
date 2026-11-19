package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.remote.api.EstoicaApiService
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.model.CitacaoDto
import kotlinx.coroutines.CancellationException

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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("EstoicaRepository", "Não foi possível buscar as citações.", e)
            Result.failure(Exception("Não foi possível buscar as citações. Verifique sua conexão."))
        }
    }
}

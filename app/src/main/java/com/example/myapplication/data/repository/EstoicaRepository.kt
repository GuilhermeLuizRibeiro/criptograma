package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.remote.api.EstoicaApiService
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.model.CitacaoDto
import com.google.gson.JsonParseException
import java.io.IOException
import retrofit2.HttpException

class EstoicaRepository(
    private val apiService: EstoicaApiService = RetrofitClient.estoicaApiService,
) {
    suspend fun buscarCitacoes(termo: String): Result<List<CitacaoDto>> {
        val termoLimpo = termo.trim().lowercase()

        return try {
            val filtradas = apiService.buscarQuotes().filter { item ->
                !item.texto.isNullOrBlank() &&
                        (item.texto.lowercase().contains(termoLimpo) ||
                                item.autor?.lowercase()?.contains(termoLimpo) == true)
            }
            Result.success(filtradas)
        } catch (e: IOException) {
            Log.e("Estoica Repository: ", "Falha de rede ao buscar citações", e)
            Result.failure(Exception("Sem conexão com a internet. Verifique a rede e tente novamente."))
        } catch (e: HttpException) {
            Log.e("Estoica Repository: ", "API respondeu com erro HTTP ${e.code()}", e)
            Result.failure(Exception("O serviço de citações respondeu com erro (${e.code()})."))
        } catch (e: JsonParseException) {
            Log.e("Estoica Repository: ", "JSON da API não bate com o CitacaoDto", e)
            Result.failure(Exception("O serviço de citações devolveu uma resposta inesperada."))
        }
    }
}
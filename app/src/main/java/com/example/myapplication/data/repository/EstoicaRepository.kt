package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.api.EstoicaApiService
import com.example.myapplication.data.remote.api.RetrofitClient
import com.example.myapplication.data.remote.model.CitacaoDto

class EstoicaRepository(
    private val apiService: EstoicaApiService = RetrofitClient.estoicaApiService,
) {
    private val frasesEstoicasFallback = listOf(
        CitacaoDto(autor = "Sêneca", texto = "A sorte é o que acontece quando a preparação encontra a oportunidade."),
        CitacaoDto(autor = "Marco Aurélio", texto = "Você tem poder sobre sua mente - não sobre eventos externos."),
        CitacaoDto(autor = "Epiteto", texto = "Não é o que acontece com você, mas como você reage que importa.")
    )

    suspend fun buscarCitacoes(termo: String): List<CitacaoDto> {
        val termoLimpo = termo.trim().lowercase()

        return try {
            val lista = apiService.buscarQuotes()
            val filtradas = lista.filter { item ->
                !item.texto.isNullOrBlank() &&
                        (item.texto.lowercase().contains(termoLimpo) || item.autor?.lowercase()?.contains(termoLimpo) == true)
            }

            if (filtradas.isNotEmpty()) {
                filtradas
            } else if (lista.isNotEmpty()) {
                lista.take(5)
            } else {
                frasesEstoicasFallback
            }
        } catch (e: Exception) {
            frasesEstoicasFallback
        }
    }
}

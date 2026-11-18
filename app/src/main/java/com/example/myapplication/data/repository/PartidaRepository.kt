package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.dao.PartidaDAO
import com.example.myapplication.data.local.entity.Partida
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CancellationException

class PartidaRepository(private val dao: PartidaDAO) {

    fun listarTodos(): Flow<List<Partida>> = dao.listarTodos()

    suspend fun buscarPorId(id: Int): Result<Partida?> {
        return try {
            Result.success(dao.buscarPorId(id))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PartidaRepository", "Não foi possível buscar a partida.", e)
            Result.failure(Exception("Não foi possível buscar a partida."))
        }
    }

    suspend fun inserir(partida: Partida): Result<Long> {
        return try {
            Result.success(dao.inserir(partida))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PartidaRepository", "Não foi possível inserir a partida.", e)
            Result.failure(Exception("Não foi possível inserir a partida."))
        }
    }

    suspend fun atualizar(partida: Partida): Result<Unit> {
        return try {
            Result.success(dao.atualizar(partida))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PartidaRepository", "Não foi possível atualizar a partida.", e)
            Result.failure(Exception("Não foi possível atualizar a partida."))
        }
    }
}
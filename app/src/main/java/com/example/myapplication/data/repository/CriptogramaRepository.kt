package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.dao.CriptogramaDAO
import com.example.myapplication.data.local.entity.Criptograma
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

class CriptogramaRepository(private val dao: CriptogramaDAO) {

    fun listarTodos(): Flow<List<Criptograma>> {
        return dao.listarTodos()
    }

    suspend fun buscarPorId(id: Int): Result<Criptograma?> {
        return try {
            Result.success(dao.buscarPorId(id))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CriptogramaRepository", "Não foi possível buscar o criptograma.", e)
            Result.failure(Exception("Não foi possível buscar o criptograma."))
        }
    }

    suspend fun salvar(criptograma: Criptograma): Result<Unit> {
        return try {
            Result.success(
                if (criptograma.id > 0) {
                    dao.atualizar(criptograma)
                } else {
                    dao.inserir(criptograma)
                }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CriptogramaRepository", "Não foi possível salvar o criptograma.", e)
            Result.failure(Exception("Não foi possível salvar o criptograma."))
        }
    }

    suspend fun atualizar(criptograma: Criptograma): Result<Unit> {
        return try {
            Result.success(dao.atualizar(criptograma))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CriptogramaRepository", "Não foi possível atualizar o criptograma.", e)
            Result.failure(Exception("Não foi possível atualizar o criptograma."))
        }
    }

    suspend fun excluir(criptograma: Criptograma): Result<Unit> {
        return try {
            Result.success(dao.excluir(criptograma))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("CriptogramaRepository", "Não foi possível excluir o criptograma.", e)
            Result.failure(Exception("Não foi possível excluir o criptograma."))
        }
    }
}

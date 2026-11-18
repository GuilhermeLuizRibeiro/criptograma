package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.PartidaDAO
import com.example.myapplication.data.local.entity.Partida
import kotlinx.coroutines.flow.Flow

class PartidaRepository(private val dao: PartidaDAO) {

    fun listarTodos(): Flow<List<Partida>> = dao.listarTodos()

    suspend fun buscarPorId(id: Int): Partida? = dao.buscarPorId(id)

    suspend fun inserir(partida: Partida): Long = dao.inserir(partida)

    suspend fun atualizar(partida: Partida) = dao.atualizar(partida)
}
package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.PartidaDAO
import com.example.myapplication.data.local.entity.Partida
import kotlinx.coroutines.flow.Flow

class PartidaRepository(private val dao: PartidaDAO) {

    fun listarTodos(): Flow<List<Partida>> = dao.listarTodos()
}
package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.data.local.entity.Partida
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDAO {
    @Insert
    suspend fun inserir(partida: Partida): Long

    @Update
    suspend fun atualizar(partida: Partida)

    @Delete
    suspend fun excluir(partida: Partida)

    @Query("SELECT * FROM partidas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Partida?

    @Query("SELECT * FROM partidas ORDER BY id DESC")
    fun listarTodos(): Flow<List<Partida>>
}
package com.example.myapplication.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade
import kotlinx.coroutines.flow.Flow

@Dao
interface CriptogramaDAO {
    @Insert
    suspend fun inserir(criptograma: Criptograma)

    @Update
    suspend fun atualizar(criptograma: Criptograma)

    @Delete
    suspend fun excluir(criptograma: Criptograma)

    @Query("SELECT * FROM criptogramas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Criptograma?

    @Query("SELECT * FROM criptogramas ORDER BY id DESC")
    fun listarTodos(): Flow<List<Criptograma>>

    @Query("""
        SELECT * FROM criptogramas
        WHERE (:dificuldade IS NULL OR dificuldade = :dificuldade)
          AND (:categoria IS NULL OR categoria = :categoria)
        ORDER BY id
    """)
    fun filtrar(dificuldade: Dificuldade?, categoria: String?): Flow<List<Criptograma>>
}
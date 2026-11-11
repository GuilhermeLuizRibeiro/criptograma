package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CriptogramaDAO
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade
import kotlinx.coroutines.flow.Flow

class CriptogramaRepository(private val dao: CriptogramaDAO) {

    fun listarTodos(): Flow<List<Criptograma>> = dao.listarTodos()

    fun filtrar(dificuldade: Dificuldade?, categoria: String?): Flow<List<Criptograma>> =
        dao.filtrar(dificuldade, categoria)

    suspend fun buscarPorId(id: Int): Criptograma? = dao.buscarPorId(id)

    suspend fun salvar(criptograma: Criptograma) {
        if (criptograma.id > 0) {
            dao.atualizar(criptograma)
        } else {
            dao.inserir(criptograma)
        }
    }

    suspend fun atualizar(criptograma: Criptograma) = dao.atualizar(criptograma)

    suspend fun excluir(criptograma: Criptograma) = dao.excluir(criptograma)
}

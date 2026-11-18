package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.dao.UsuarioDAO
import com.example.myapplication.data.local.entity.Usuario
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CancellationException

class UsuarioRepository(private val dao: UsuarioDAO) {

    fun listarTodos(): Flow<List<Usuario>> {
        return dao.listarTodos()
    }

    suspend fun buscarPorId(id: Int): Result<Usuario?> {
        return try {
            Result.success(dao.buscarPorId(id))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Não foi possível buscar o usuário.", e)
            Result.failure(Exception("Não foi possível buscar o usuário."))
        }
    }

    suspend fun salvar(usuario: Usuario): Result<Unit> {
        return try {
            Result.success(
                if (usuario.id > 0) {
                    dao.atualizar(usuario)
                } else {
                    dao.inserir(usuario)
                }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Não foi possível salvar o usuário.", e)
            Result.failure(Exception("Não foi possível salvar o usuário."))
        }

    }

    suspend fun garantirUsuarioDefault(papel: Papel): Result<Usuario> {
        return try {
            val emailDefault = if (papel == Papel.ADMIN) "admin@criptograma.com" else "jogador@criptograma.com"
            val usuarioExistente = dao.buscarPorEmail(emailDefault)
            if (usuarioExistente != null) {
                return Result.success(usuarioExistente)
            }
            val novoUsuario = Usuario(
                nome = if (papel == Papel.ADMIN) "Administrador" else "Jogador",
                email = emailDefault,
                papel = papel
            )
            dao.inserir(novoUsuario)
            Result.success(dao.buscarPorEmail(emailDefault) ?: novoUsuario)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Não foi possível entrar.", e)
            Result.failure(Exception("Não foi possível entrar."))
        }
    }
    fun listarRanking(): List<Usuario> = dao.listarRanking()
}
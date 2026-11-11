package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UsuarioDAO
import com.example.myapplication.data.local.entity.Usuario
import com.example.myapplication.model.enums.Papel
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDAO) {

    fun listarTodos(): Flow<List<Usuario>> = dao.listarTodos()

    suspend fun salvar(usuario: Usuario) {
        if (usuario.id > 0) {
            dao.atualizar(usuario)
        } else {
            dao.inserir(usuario)
        }
    }

    suspend fun garantirUsuarioDefault(papel: Papel): Usuario {
        val emailDefault = if (papel == Papel.ADMIN) "admin@criptograma.com" else "jogador@criptograma.com"
        val usuarioExistente = dao.buscarPorEmail(emailDefault)
        if (usuarioExistente != null) {
            return usuarioExistente
        }
        val novoUsuario = Usuario(
            nome = if (papel == Papel.ADMIN) "Administrador" else "Jogador",
            email = emailDefault,
            papel = papel
        )
        dao.inserir(novoUsuario)
        return dao.buscarPorEmail(emailDefault) ?: novoUsuario
    }
}

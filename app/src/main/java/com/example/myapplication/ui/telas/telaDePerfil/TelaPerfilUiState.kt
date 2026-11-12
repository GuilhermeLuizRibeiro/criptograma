package com.example.myapplication.ui.telas.telaDePerfil

import com.example.myapplication.data.local.entity.Usuario
import com.example.myapplication.model.enums.Papel

data class TelaPerfilUiState(
    val usuario: Usuario? = null,
    val itemNavegacaoSelecionado: String = "Perfil",
) {
    val ehAdmin: Boolean
        get() = usuario?.papel == Papel.ADMIN

    val nomeExibicao: String
        get() = usuario?.nome ?: "Usuário"

    val emailExibicao: String
        get() = usuario?.email ?: "usuario@email.com"
}

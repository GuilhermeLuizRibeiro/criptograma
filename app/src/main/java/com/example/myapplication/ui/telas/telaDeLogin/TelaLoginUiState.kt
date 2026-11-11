package com.example.myapplication.ui.telas.telaDeLogin

import com.example.myapplication.data.local.entity.Usuario

data class TelaLoginUiState(
    val usuarioLogado: Usuario? = null,
    val carregando: Boolean = false,
)

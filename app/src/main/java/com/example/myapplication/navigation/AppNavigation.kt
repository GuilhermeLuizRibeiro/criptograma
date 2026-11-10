package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.telas.telaCriarCriptograma.TelaCriaCriptogramasAdmin
import com.example.myapplication.ui.telas.telaDeLogin.TelaLogin
import com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma.TelaListaCriptogramasAdmin

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rotas.TELA_LOGIN
    ) {
        composable(Rotas.TELA_LOGIN) {
            TelaLogin(
                onAdminClick = {
                    navController.navigate(Rotas.TELA_LISTA_ADMIN)
                },
                onJogadorClick = {
                    // Rota para o jogador
                }
            )
        }

        composable(Rotas.TELA_LISTA_ADMIN) {
            TelaListaCriptogramasAdmin(
                onNovoCasoClick = {
                    navController.navigate(Rotas.TELA_CRIA_ADMIN)
                }
            )
        }

        composable(Rotas.TELA_CRIA_ADMIN) {
            TelaCriaCriptogramasAdmin(
                onSalvarClick = {
                    navController.popBackStack()
                },
                onCancelarClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

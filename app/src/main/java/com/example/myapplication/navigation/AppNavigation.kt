package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.telas.telaDeInicio.TelaInicio
import com.example.myapplication.ui.telas.telaDoAdmin.TelaCriaCriptogramasAdmin
import com.example.myapplication.ui.telas.telaDoAdmin.TelaListaCriptogramasAdmin

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rotas.TELA_INICIO
    ) {
        composable(Rotas.TELA_INICIO) {
            TelaInicio(
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

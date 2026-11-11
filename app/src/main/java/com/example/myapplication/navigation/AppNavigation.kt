package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                },
                onEditarClick = { cripto ->
                    navController.navigate("${Rotas.TELA_CRIA_ADMIN}?id=${cripto.id}")
                }
            )
        }

        composable(
            route = "${Rotas.TELA_CRIA_ADMIN}?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            TelaCriaCriptogramasAdmin(
                criptogramaId = if (id > 0) id else null,
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

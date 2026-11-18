package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.model.enums.Papel
import com.example.myapplication.ui.telas.casos.TelaCasos
import com.example.myapplication.ui.telas.inicio.TelaInicio
import com.example.myapplication.ui.telas.jogo.TelaJogo
import com.example.myapplication.ui.telas.resultado.TelaResultado
import com.example.myapplication.ui.telas.telaCriarCriptograma.TelaCriaCriptogramasAdmin
import com.example.myapplication.ui.telas.telaDeLogin.TelaLogin
import com.example.myapplication.ui.telas.telaDePerfil.TelaPerfil
import com.example.myapplication.ui.telas.telaListaEditaExcluiCriptograma.TelaListaCriptogramasAdmin

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val voltarParaInicio: () -> Unit = {
        navController.navigate(Rotas.TELA_INICIO) {
            popUpTo(Rotas.TELA_INICIO) { inclusive = true }
        }
    }

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
                    navController.navigate(Rotas.TELA_INICIO)
                }
            )
        }

        composable(Rotas.TELA_INICIO) {
            TelaInicio(
                onItemNavegacaoClick = { item ->
                    if (item == "Perfil") {
                        navController.navigate(Rotas.TELA_PERFIL)
                    } else if(item == "Casos") {
                        navController.navigate(Rotas.TELA_CASOS)
                    }
                },
                onContinuarClick = {
                    navController.navigate(Rotas.TELA_JOGO)
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
                },
                onItemNavegacaoClick = { item ->
                    if (item == "Perfil") {
                        navController.navigate("${Rotas.TELA_PERFIL}/${Papel.ADMIN.name}")
                    }
                }
            )
        }

        composable(
            route = "${Rotas.TELA_PERFIL}/{papel}",
            arguments = listOf(
                navArgument("papel") {
                    type = NavType.StringType
                    defaultValue = Papel.ADMIN.name
                }
            )
        ) { backStackEntry ->
            val papelString = backStackEntry.arguments?.getString("papel") ?: Papel.ADMIN.name
            val papel = try {
                Papel.valueOf(papelString)
            } catch (e: Exception) {
                Papel.ADMIN
            }

            TelaPerfil(
                papel = papel,
                onItemNavegacaoClick = { item ->
                    if (item == "Painel") {
                        navController.navigate(Rotas.TELA_LISTA_ADMIN) {
                            popUpTo(Rotas.TELA_LISTA_ADMIN) { inclusive = true }
                        }
                    } else if (item == "Início") {
                        navController.navigate(Rotas.TELA_INICIO) {
                            popUpTo(Rotas.TELA_INICIO) { inclusive = true }
                        }
                    } else if (item == "Casos") {
                        navController.navigate(Rotas.TELA_CASOS) {
                            popUpTo(Rotas.TELA_CASOS) { inclusive = true }
                        }
                    }
                },
                onSairClick = {
                    navController.navigate(Rotas.TELA_LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
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

        composable(Rotas.TELA_CASOS) {
            TelaCasos(
                onItemNavegacaoClick = { item ->
                    when (item) {
                        "Início" -> navController.navigate(Rotas.TELA_INICIO) {
                            popUpTo(Rotas.TELA_INICIO) { inclusive = true }
                        }
                        "Perfil" -> navController.navigate("${Rotas.TELA_PERFIL}/${Papel.USUARIO.name}")
                    }
                },
                onCasoClick = { criptograma ->
                    navController.navigate("${Rotas.TELA_JOGO}?id=${criptograma.id}")
                }
            )
        }

        composable(
            route = "${Rotas.TELA_JOGO}?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            TelaJogo(
                criptogramaId = if (id > 0) id else null,
                onPausarClick = voltarParaInicio,
                onCasoDecifrado = { partidaId ->
                    navController.navigate("${Rotas.TELA_RESULTADO}/$partidaId") {
                        popUpTo("${Rotas.TELA_JOGO}?id={id}") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Rotas.TELA_RESULTADO}/{partidaId}",
            arguments = listOf(
                navArgument("partidaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val partidaId = backStackEntry.arguments?.getInt("partidaId") ?: -1
            TelaResultado(
                partidaId = partidaId,
                onVoltarInicioClick = voltarParaInicio
            )
        }
    }
}
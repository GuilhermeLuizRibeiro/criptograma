package com.example.myapplication.ui.telas.telaDeLogin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.acao
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.sobreAcao

@Composable
fun TelaLogin(
    modifier: Modifier = Modifier,
    onAdminClick: () -> Unit = {},
    onJogadorClick: () -> Unit = {},
    viewModel: TelaLoginViewModel = viewModel(
        factory = TelaLoginViewModelFactory(
            UsuarioRepository(AppDatabase.getDatabase(LocalContext.current).usuarioDAO())
        )
    ),
) {
    val navegarParaAdmin = onAdminClick
    val navegarParaJogador = onJogadorClick

    TelaLoginContent(
        modifier = modifier,
        onAdminClick = {
            viewModel.entrarComoAdmin(onSucesso = navegarParaAdmin)
        },
        onJogadorClick = {
            viewModel.entrarComoJogador(onSucesso = navegarParaJogador)
        }
    )
}

@Composable
fun TelaLoginContent(
    modifier: Modifier = Modifier,
    onAdminClick: () -> Unit = {},
    onJogadorClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .fundoQuadriculado(),
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = ficha
                ),
                border = BorderStroke(2.dp, CorBordaCard),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(0.85f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 36.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onAdminClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = acao,
                            contentColor = sobreAcao
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "Admin",
                            fontFamily = CourierPrime,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Button(
                        onClick = onJogadorClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = acao,
                            contentColor = sobreAcao
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "Jogador",
                            fontFamily = CourierPrime,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaInicioPreview() {
    TelaLoginContent()
}

package com.example.myapplication.ui.telas.telaDeLogin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorBotaoRoxo
import com.example.myapplication.ui.theme.CorFundoCard
import com.example.myapplication.ui.theme.CorFundoTela

@Composable
fun TelaLogin(
    modifier: Modifier = Modifier,
    viewModel: TelaLoginViewModel = viewModel(),
    onAdminClick: () -> Unit = {},
    onJogadorClick: () -> Unit = {},
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CorFundoTela),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CorFundoCard
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
                            containerColor = CorBotaoRoxo,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "Admin",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Button(
                        onClick = onJogadorClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CorBotaoRoxo,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = "Jogador",
                            fontFamily = FontFamily.Monospace,
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

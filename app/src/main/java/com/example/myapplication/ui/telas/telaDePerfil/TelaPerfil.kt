package com.example.myapplication.ui.telas.telaDePerfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.componentes.NavegacaoRodapeAdmin
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorFundoCard
import com.example.myapplication.ui.theme.CorFundoTela
import com.example.myapplication.ui.theme.CorInputFundo
import com.example.myapplication.ui.theme.CorTextoEscuro

@Composable
fun TelaPerfil(
    modifier: Modifier = Modifier,
    viewModel: TelaPerfilViewModel = viewModel(),
    onItemNavegacaoClick: (String) -> Unit = {},
    onSairClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    TelaPerfilContent(
        uiState = uiState,
        modifier = modifier,
        onItemNavegacaoClick = { item ->
            viewModel.onItemNavegacaoChange(item)
            onItemNavegacaoClick(item)
        },
        onSairClick = onSairClick
    )
}

@Composable
fun TelaPerfilContent(
    uiState: TelaPerfilUiState,
    modifier: Modifier = Modifier,
    onItemNavegacaoClick: (String) -> Unit = {},
    onSairClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = CorFundoTela,
        bottomBar = {
            if (uiState.ehAdmin) {
                NavegacaoRodapeAdmin(
                    itemSelecionado = uiState.itemNavegacaoSelecionado,
                    onItemClick = onItemNavegacaoClick
                )
            } else {
                // Aqui ficará o rodapé do Jogador quando for adicionado
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Título Topo "Perfil"
            Text(
                text = "Perfil",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = CorTextoEscuro,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Card com informações do usuário
            Card(
                colors = CardDefaults.cardColors(containerColor = CorFundoCard),
                border = BorderStroke(2.dp, CorBordaCard),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Moldura de foto
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CorInputFundo),
                        border = BorderStroke(1.dp, CorBordaCard),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.size(width = 62.dp, height = 70.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconeAvatarPerfil(tint = CorTextoEscuro)
                        }
                    }

                    // Informações Nome e E-mail
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nome",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = CorTextoEscuro
                            )
                            Text(
                                text = uiState.nomeExibicao,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CorTextoEscuro
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "E-mail",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = CorTextoEscuro
                            )
                            Text(
                                text = uiState.emailExibicao,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CorTextoEscuro
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão "Sair" (Logout)
            Card(
                colors = CardDefaults.cardColors(containerColor = CorInputFundo),
                border = BorderStroke(1.dp, CorBordaCard),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onSairClick() }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sair",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = CorTextoEscuro
                    )
                }
            }
        }
    }
}

@Composable
private fun IconeAvatarPerfil(tint: Color) {
    Canvas(modifier = Modifier.size(36.dp)) {
        drawCircle(
            color = tint,
            radius = size.width * 0.22f,
            center = Offset(size.width * 0.5f, size.height * 0.35f)
        )
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.9f)
            cubicTo(
                size.width * 0.15f, size.height * 0.62f,
                size.width * 0.85f, size.height * 0.62f,
                size.width * 0.85f, size.height * 0.9f
            )
        }
        drawPath(path, color = tint)
    }
}

@Preview(showBackground = true)
@Composable
fun TelaPerfilPreview() {
    TelaPerfilContent(
        uiState = TelaPerfilUiState()
    )
}

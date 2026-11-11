package com.example.myapplication.ui.telas.telaCriarCriptograma

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.ui.theme.CorBadgeEscuro
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorBotaoRoxo
import com.example.myapplication.ui.theme.CorFundoCard
import com.example.myapplication.ui.theme.CorFundoTela
import com.example.myapplication.ui.theme.CorInputFundo
import com.example.myapplication.ui.theme.CorTextoEscuro
import com.example.myapplication.ui.theme.CorTextoSecundario
import com.example.myapplication.ui.theme.CorTextoVermelho

@Composable
fun TelaCriaCriptogramasAdmin(
    modifier: Modifier = Modifier,
    criptogramaId: Int? = null,
    viewModel: TelaCriaCriptogramasAdminViewModel = viewModel(),
    onSalvarClick: (Criptograma) -> Unit = {},
    onCancelarClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(criptogramaId) {
        if (criptogramaId != null && criptogramaId > 0) {
            viewModel.carregarCriptogramaPorId(criptogramaId)
        }
    }

    TelaCriaCriptogramasAdminContent(
        uiState = uiState,
        modifier = modifier,
        onFraseChange = viewModel::onFraseChange,
        onAutorChange = viewModel::onAutorChange,
        onCategoriaChange = viewModel::onCategoriaSelecionadaChange,
        onDificuldadeChange = viewModel::onDificuldadeSelecionadaChange,
        onOrigemChange = viewModel::onOrigemSelecionadaChange,
        onMenuExpandidoChange = viewModel::onMenuCategoriaExpandidoChange,
        onSalvarClick = {
            viewModel.salvarCriptograma(onSucesso = onSalvarClick)
        },
        onCancelarClick = onCancelarClick
    )
}

@Composable
fun TelaCriaCriptogramasAdminContent(
    uiState: TelaCriaCriptogramasAdminUiState,
    modifier: Modifier = Modifier,
    onFraseChange: (String) -> Unit = {},
    onAutorChange: (String) -> Unit = {},
    onCategoriaChange: (String) -> Unit = {},
    onDificuldadeChange: (Dificuldade) -> Unit = {},
    onOrigemChange: (String) -> Unit = {},
    onMenuExpandidoChange: (Boolean) -> Unit = {},
    onSalvarClick: () -> Unit = {},
    onCancelarClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = CorFundoTela,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Cabeçalho Topo: "Novo caso" / "Editar caso" à esquerda, "Cancelar" à direita
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.editando) "Editar caso" else "Novo caso",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CorTextoEscuro
                )
                Text(
                    text = "Cancelar",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    color = CorTextoEscuro,
                    modifier = Modifier.clickable { onCancelarClick() }
                )
            }

            // Card Formulário Principal
            Card(
                colors = CardDefaults.cardColors(containerColor = CorFundoCard),
                border = BorderStroke(2.dp, CorBordaCard),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Campo 1: Frase em português (tradução)
                    Column {
                        Text(
                            text = "Frase em português (a que o jogador decifra)",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = CorTextoEscuro,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = uiState.frase,
                            onValueChange = onFraseChange,
                            placeholder = {
                                Text(
                                    text = "Digite a tradução",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = CorTextoSecundario
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CorInputFundo,
                                unfocusedContainerColor = CorInputFundo,
                                focusedBorderColor = CorBordaCard,
                                unfocusedBorderColor = CorBordaCard,
                                focusedTextColor = CorTextoEscuro,
                                unfocusedTextColor = CorTextoEscuro
                            ),
                            minLines = 3,
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (uiState.tentouSalvar && uiState.erros.frase != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.erros.frase ?: "Obrigatória para salvar.",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CorTextoVermelho
                            )
                        }
                    }

                    // Campo 2: Autor com moldura de foto
                    Column {
                        Text(
                            text = "Autor",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = CorTextoEscuro,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CorInputFundo),
                                border = BorderStroke(1.dp, CorBordaCard),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.size(width = 52.dp, height = 58.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconeAvatarAutor(tint = CorTextoEscuro)
                                }
                            }

                            OutlinedTextField(
                                value = uiState.autor,
                                onValueChange = onAutorChange,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CorInputFundo,
                                    unfocusedContainerColor = CorInputFundo,
                                    focusedBorderColor = CorBordaCard,
                                    unfocusedBorderColor = CorBordaCard,
                                    focusedTextColor = CorTextoEscuro,
                                    unfocusedTextColor = CorTextoEscuro
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Campo 3: Categoria (Dropdown)
                    Column {
                        Text(
                            text = "Categoria",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = CorTextoEscuro,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CorInputFundo, RoundedCornerShape(4.dp))
                                    .border(1.dp, CorBordaCard, RoundedCornerShape(4.dp))
                                    .clickable { onMenuExpandidoChange(true) }
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiState.categoriaSelecionada,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = CorTextoEscuro
                                )
                                IconeSetaBaixo(tint = CorTextoEscuro)
                            }

                            DropdownMenu(
                                expanded = uiState.menuCategoriaExpandido,
                                onDismissRequest = { onMenuExpandidoChange(false) },
                                modifier = Modifier.background(CorInputFundo)
                            ) {
                                uiState.categoriasDisponiveis.forEach { cat ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = cat,
                                                fontFamily = FontFamily.Monospace,
                                                color = CorTextoEscuro
                                            )
                                        },
                                        onClick = { onCategoriaChange(cat) }
                                    )
                                }
                            }
                        }
                    }

                    // Campo 4: Dificuldade (Botões Segmentados)
                    Column {
                        Text(
                            text = "Dificuldade",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = CorTextoEscuro,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CorBordaCard, RoundedCornerShape(4.dp))
                        ) {
                            val dificuldades = listOf(
                                Pair(Dificuldade.FACIL, "Fácil"),
                                Pair(Dificuldade.MEDIO, "Médio"),
                                Pair(Dificuldade.DIFICIL, "Difícil")
                            )

                            dificuldades.forEachIndexed { index, (dif, label) ->
                                val selecionado = dif == uiState.dificuldadeSelecionada
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(if (selecionado) CorBadgeEscuro else CorInputFundo)
                                        .clickable { onDificuldadeChange(dif) }
                                        .border(
                                            width = if (index < dificuldades.size - 1) 1.dp else 0.dp,
                                            color = CorBordaCard
                                        )
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = if (selecionado) Color.White else CorTextoEscuro
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Campo 5: Origem (Manual / API de citações)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Origem: ${uiState.origemSelecionada}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = CorTextoSecundario
                        )
                        Text(
                            text = if (uiState.origemSelecionada == "Manual") "Mudar p/ API" else "Mudar p/ Manual",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CorTextoEscuro,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val nova = if (uiState.origemSelecionada == "Manual") "API de citações" else "Manual"
                                onOrigemChange(nova)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão "Salvar caso"
            Button(
                onClick = onSalvarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CorBotaoRoxo,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Salvar caso",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun IconeAvatarAutor(tint: Color) {
    Canvas(modifier = Modifier.size(32.dp)) {
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

@Composable
private fun IconeSetaBaixo(tint: Color) {
    Canvas(modifier = Modifier.size(12.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.3f)
            lineTo(size.width * 0.5f, size.height * 0.8f)
            lineTo(size.width, size.height * 0.3f)
        }
        drawPath(path, color = tint, style = Stroke(width = 2.dp.toPx()))
    }
}

@Preview(showBackground = true)
@Composable
fun TelaCriaCriptogramasAdminPreview() {
    TelaCriaCriptogramasAdminContent(
        uiState = TelaCriaCriptogramasAdminUiState()
    )
}

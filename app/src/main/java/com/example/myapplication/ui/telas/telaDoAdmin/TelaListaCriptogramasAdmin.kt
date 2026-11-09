package com.example.myapplication.ui.telas.telaDoAdmin

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
fun TelaListaCriptogramasAdmin(
    modifier: Modifier = Modifier,
    viewModel: TelaListaCriptogramasAdminViewModel = viewModel(),
    onNovoCasoClick: () -> Unit = {},
    onEditarClick: (Criptograma) -> Unit = {},
    onExcluirClick: (Criptograma) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    TelaListaCriptogramasAdminContent(
        uiState = uiState,
        modifier = modifier,
        onBuscaChange = viewModel::onBuscaChange,
        onFiltroChange = viewModel::onFiltroSelecionadoChange,
        onItemNavegacaoChange = viewModel::onItemNavegacaoChange,
        onNovoCasoClick = onNovoCasoClick,
        onEditarClick = onEditarClick,
        onExcluirClick = { cripto ->
            viewModel.excluirCriptograma(cripto)
            onExcluirClick(cripto)
        }
    )
}

@Composable
fun TelaListaCriptogramasAdminContent(
    uiState: TelaListaCriptogramasAdminUiState,
    modifier: Modifier = Modifier,
    onBuscaChange: (String) -> Unit = {},
    onFiltroChange: (String) -> Unit = {},
    onItemNavegacaoChange: (String) -> Unit = {},
    onNovoCasoClick: () -> Unit = {},
    onEditarClick: (Criptograma) -> Unit = {},
    onExcluirClick: (Criptograma) -> Unit = {},
) {
    // Filtra a lista conforme busca e aba de dificuldade selecionada
    val listaFiltrada = uiState.listaCriptogramas.filter { item ->
        val bateFiltroBusca = uiState.busca.isBlank() ||
                item.fraseTraducao.contains(uiState.busca, ignoreCase = true) ||
                item.autor.contains(uiState.busca, ignoreCase = true)

        val bateFiltroDificuldade = when (uiState.filtroSelecionado) {
            "Fácil" -> item.dificuldade == Dificuldade.FACIL
            "Médio" -> item.dificuldade == Dificuldade.MEDIO
            "Difícil" -> item.dificuldade == Dificuldade.DIFICIL
            else -> true
        }

        bateFiltroBusca && bateFiltroDificuldade
    }

    Scaffold(
        containerColor = CorFundoTela,
        bottomBar = {
            NavegacaoInferiorAdmin(
                itemSelecionado = uiState.itemNavegacaoSelecionado,
                onItemClick = onItemNavegacaoChange
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Cabeçalho Topo: "Casos" e número de casos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Casos",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CorTextoEscuro
                )
                Text(
                    text = "${listaFiltrada.size * 10 + 2} casos",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = CorTextoSecundario
                )
            }

            // Abas de Filtro (Todos, Fácil, Médio, Difícil)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                val opcoesFiltro = listOf("Todos", "Fácil", "Médio", "Difícil")
                opcoesFiltro.forEach { opcao ->
                    val selecionado = opcao == uiState.filtroSelecionado
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selecionado) CorFundoCard else CorFundoTela,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = if (selecionado) 2.dp else 1.dp,
                                    color = CorBordaCard
                                ),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                            .clickable { onFiltroChange(opcao) }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = opcao,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = CorTextoEscuro
                        )
                    }
                }
            }

            // Card Principal que envolve a busca e os criptogramas
            Card(
                colors = CardDefaults.cardColors(containerColor = CorFundoCard),
                border = BorderStroke(2.dp, CorBordaCard),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topEnd = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Campo de Busca
                    OutlinedTextField(
                        value = uiState.busca,
                        onValueChange = onBuscaChange,
                        placeholder = {
                            Text(
                                text = "Buscar frase ou autor",
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
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    // Lista dos Criptogramas
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(listaFiltrada) { criptograma ->
                            ItemCriptogramaAdmin(
                                criptograma = criptograma,
                                onEditar = { onEditarClick(criptograma) },
                                onExcluir = { onExcluirClick(criptograma) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão "Novo caso"
            Button(
                onClick = onNovoCasoClick,
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
                    text = "Novo caso",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ItemCriptogramaAdmin(
    criptograma: Criptograma,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CorFundoCard),
        border = BorderStroke(1.dp, CorBordaCard),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Linha superior: Número/Dificuldade à esquerda, Origem à direita
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CorInputFundo),
                    border = BorderStroke(1.dp, CorBordaCard),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(CorTextoEscuro, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Nº ${criptograma.id.toString().padStart(4, '0')}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CorTextoEscuro
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .background(CorBadgeEscuro, RoundedCornerShape(2.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when (criptograma.dificuldade) {
                                    Dificuldade.FACIL -> "FÁCIL"
                                    Dificuldade.MEDIO -> "MÉDIO"
                                    Dificuldade.DIFICIL -> "DIFÍCIL"
                                },
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Text(
                    text = criptograma.idExterno ?: "Manual",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = CorTextoSecundario
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "“${criptograma.fraseTraducao}”",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CorTextoEscuro
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${criptograma.autor} · ${criptograma.categoria}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = CorTextoSecundario
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Editar",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = CorTextoEscuro,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onEditar() }
                    )
                    Text(
                        text = "Excluir",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = CorTextoVermelho,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onExcluir() }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavegacaoInferiorAdmin(
    itemSelecionado: String,
    onItemClick: (String) -> Unit = {},
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CorFundoCard),
        border = BorderStroke(1.dp, CorBordaCard),
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val itens = listOf("Painel", "Casos", "Jogar", "Perfil")

            itens.forEach { titulo ->
                val ativo = titulo == itemSelecionado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onItemClick(titulo) }
                ) {
                    if (ativo) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(3.dp)
                                .background(CorBotaoRoxo, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    } else {
                        Spacer(modifier = Modifier.height(7.dp))
                    }

                    when (titulo) {
                        "Painel" -> PainelIcon(CorTextoEscuro)
                        "Casos" -> CasosIcon(CorTextoEscuro)
                        "Jogar" -> JogarIcon(CorTextoEscuro)
                        "Perfil" -> PerfilIcon(CorTextoEscuro)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = titulo,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = if (ativo) FontWeight.Bold else FontWeight.Normal,
                        color = CorTextoEscuro
                    )
                }
            }
        }
    }
}

@Composable
private fun PainelIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val gap = 3.dp.toPx()
        val half = (size.width - gap) / 2
        drawRoundRect(tint, topLeft = Offset(0f, 0f), size = Size(half, half), cornerRadius = CornerRadius(4f))
        drawRoundRect(tint, topLeft = Offset(half + gap, 0f), size = Size(half, half), cornerRadius = CornerRadius(4f))
        drawRoundRect(tint, topLeft = Offset(0f, half + gap), size = Size(half, half), cornerRadius = CornerRadius(4f))
        drawRoundRect(tint, topLeft = Offset(half + gap, half + gap), size = Size(half, half), cornerRadius = CornerRadius(4f))
    }
}

@Composable
private fun CasosIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.25f)
            lineTo(size.width * 0.4f, size.height * 0.25f)
            lineTo(size.width * 0.55f, size.height * 0.4f)
            lineTo(size.width, size.height * 0.4f)
            lineTo(size.width, size.height * 0.9f)
            lineTo(0f, size.height * 0.9f)
            close()
        }
        drawPath(path, color = tint, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
private fun JogarIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.15f)
            lineTo(size.width * 0.85f, size.height * 0.5f)
            lineTo(size.width * 0.2f, size.height * 0.85f)
            close()
        }
        drawPath(path, color = tint)
    }
}

@Composable
private fun PerfilIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val strokeWidth = 2.dp.toPx()
        drawCircle(
            color = tint,
            radius = size.width * 0.22f,
            center = Offset(size.width * 0.5f, size.height * 0.32f),
            style = Stroke(width = strokeWidth)
        )
        val path = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.9f)
            cubicTo(
                size.width * 0.15f, size.height * 0.65f,
                size.width * 0.85f, size.height * 0.65f,
                size.width * 0.85f, size.height * 0.9f
            )
        }
        drawPath(path, color = tint, style = Stroke(width = strokeWidth))
    }
}

@Preview(showBackground = true)
@Composable
fun TelaListaCriptogramasAdminPreview() {
    TelaListaCriptogramasAdminContent(
        uiState = TelaListaCriptogramasAdminUiState(
            listaCriptogramas = listOf(
                Criptograma(
                    id = 7,
                    fraseTraducao = "O segredo para progredir é começar.",
                    autor = "Mark Twain",
                    categoria = "Citações",
                    dificuldade = Dificuldade.DIFICIL,
                    idExterno = "API"
                )
            )
        )
    )
}

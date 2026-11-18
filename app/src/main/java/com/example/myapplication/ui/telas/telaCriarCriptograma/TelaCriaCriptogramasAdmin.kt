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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.remote.model.CitacaoDto
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.EstoicaRepository
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.CorBadgeEscuro
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorTextoSecundario
import com.example.myapplication.ui.theme.CorTextoVermelho
import com.example.myapplication.ui.theme.acao
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.ficha2
import com.example.myapplication.ui.theme.sobreAcao
import com.example.myapplication.ui.theme.tinta

@Composable
fun TelaCriaCriptogramasAdmin(
    modifier: Modifier = Modifier,
    criptogramaId: Int? = null,
    onSalvarClick: (Criptograma) -> Unit = {},
    onCancelarClick: () -> Unit = {},
    viewModel: TelaCriaCriptogramasAdminViewModel = viewModel(
        factory = TelaCriaCriptogramasAdminViewModelFactory(
            CriptogramaRepository(AppDatabase.getDatabase(LocalContext.current).criptogramaDAO()),
            EstoicaRepository()
        )
    ),
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
        onTermoBuscaApiChange = viewModel::onTermoBuscaApiChange,
        onBuscarApiClick = viewModel::buscarCitacoesNaApi,
        onSelecionarCitacaoApi = viewModel::selecionarCitacaoApi,
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
    onTermoBuscaApiChange: (String) -> Unit = {},
    onBuscarApiClick: () -> Unit = {},
    onSelecionarCitacaoApi: (CitacaoDto) -> Unit = {},
    onMenuExpandidoChange: (Boolean) -> Unit = {},
    onSalvarClick: () -> Unit = {},
    onCancelarClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .fundoQuadriculado(),
        containerColor = Color.Transparent
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
                    fontFamily = CourierPrime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = tinta
                )
                Text(
                    text = "Cancelar",
                    fontFamily = CourierPrime,
                    fontSize = 15.sp,
                    color = tinta,
                    modifier = Modifier.clickable { onCancelarClick() }
                )
            }

            // Card Formulário Principal
            Card(
                colors = CardDefaults.cardColors(containerColor = ficha),
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
                    // SELETOR NO TOPO: Origem (Manual x API de citações)
                    Column {
                        Text(
                            text = "Modo de cadastro",
                            fontFamily = CourierPrime,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = tinta,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, CorBordaCard, RoundedCornerShape(4.dp))
                        ) {
                            uiState.origensDisponiveis.forEachIndexed { index, origem ->
                                val selecionado = origem == uiState.origemSelecionada
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(if (selecionado) CorBadgeEscuro else ficha2)
                                        .clickable { onOrigemChange(origem) }
                                        .border(
                                            width = if (index < uiState.origensDisponiveis.size - 1) 1.dp else 0.dp,
                                            color = CorBordaCard
                                        )
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = origem,
                                        fontFamily = CourierPrime,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = if (selecionado) Color.White else tinta
                                    )
                                }
                            }
                        }
                    }

                    // SEÇÃO API: Busca na API quando origem for "API de citações"
                    if (uiState.origemSelecionada == "API de citações") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ficha2),
                            border = BorderStroke(1.dp, CorBordaCard),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Buscar citação na Web (API de Frases Estoicas)",
                                    fontFamily = CourierPrime,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = tinta
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = uiState.termoBuscaApi,
                                        onValueChange = onTermoBuscaApiChange,
                                        placeholder = {
                                            Text(
                                                text = "Ex: virtude, Sêneca...",
                                                fontFamily = CourierPrime,
                                                fontSize = 13.sp,
                                                color = CorTextoSecundario
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = Color.White,
                                            unfocusedContainerColor = Color.White,
                                            focusedBorderColor = CorBordaCard,
                                            unfocusedBorderColor = CorBordaCard,
                                            focusedTextColor = tinta,
                                            unfocusedTextColor = tinta
                                        ),
                                        singleLine = true,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Button(
                                        onClick = onBuscarApiClick,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = acao,
                                            contentColor = sobreAcao
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.height(52.dp)
                                    ) {
                                        Text(
                                            text = "Buscar",
                                            fontFamily = CourierPrime,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }

                                if (uiState.buscandoApi) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            color = acao,
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Text(
                                            text = "Consultando API de citações...",
                                            fontFamily = CourierPrime,
                                            fontSize = 12.sp,
                                            color = CorTextoSecundario
                                        )
                                    }
                                }

                                if (uiState.mensagemErroApi != null) {
                                    Text(
                                        text = uiState.mensagemErroApi,
                                        fontFamily = CourierPrime,
                                        fontSize = 12.sp,
                                        color = CorTextoVermelho
                                    )
                                }

                                // Lista de citações retornadas da API
                                if (uiState.resultadoCitacoesApi.isNotEmpty()) {
                                    Text(
                                        text = "Clique em uma citação para usá-la no formulário:",
                                        fontFamily = CourierPrime,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tinta,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )

                                    uiState.resultadoCitacoesApi.forEach { citacao ->
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            border = BorderStroke(1.dp, CorBordaCard),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onSelecionarCitacaoApi(citacao) }
                                                .padding(vertical = 2.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                            ) {
                                                Text(
                                                    text = "“${citacao.texto ?: ""}”",
                                                    fontFamily = CourierPrime,
                                                    fontSize = 13.sp,
                                                    color = tinta
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "— ${citacao.autor ?: "Desconhecido"}",
                                                        fontFamily = CourierPrime,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = CorTextoSecundario
                                                    )
                                                    Text(
                                                        text = "Usar esta citação",
                                                        fontFamily = CourierPrime,
                                                        fontSize = 11.sp,
                                                        color = acao,
                                                        fontWeight = FontWeight.Bold,
                                                        textDecoration = TextDecoration.Underline
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Campo 1: Frase em português
                    Column {
                        Text(
                            text = "Frase em português (a que o jogador decifra)",
                            fontFamily = CourierPrime,
                            fontSize = 13.sp,
                            color = tinta,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = uiState.frase,
                            onValueChange = onFraseChange,
                            placeholder = {
                                Text(
                                    text = "Digite ou selecione uma citação acima",
                                    fontFamily = CourierPrime,
                                    fontSize = 14.sp,
                                    color = CorTextoSecundario
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ficha2,
                                unfocusedContainerColor = ficha2,
                                focusedBorderColor = if (uiState.tentouSalvar && uiState.erros.frase != null) Color.Red else Color(0xFF800020),
                                unfocusedBorderColor = if (uiState.tentouSalvar && uiState.erros.frase != null) Color.Red else Color(0xFF800020),
                                focusedTextColor = tinta,
                                unfocusedTextColor = tinta
                            ),
                            minLines = 3,
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (uiState.tentouSalvar && uiState.erros.frase != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.erros.frase ?: "Obrigatória para salvar.",
                                fontFamily = CourierPrime,
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
                            fontFamily = CourierPrime,
                            fontSize = 13.sp,
                            color = tinta,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = ficha2),
                                border = BorderStroke(1.dp, CorBordaCard),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.size(width = 52.dp, height = 58.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconeAvatarAutor(tint = tinta)
                                }
                            }

                            OutlinedTextField(
                                value = uiState.autor,
                                onValueChange = onAutorChange,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ficha2,
                                    unfocusedContainerColor = ficha2,
                                    focusedBorderColor = CorBordaCard,
                                    unfocusedBorderColor = CorBordaCard,
                                    focusedTextColor = tinta,
                                    unfocusedTextColor = tinta
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
                            fontFamily = CourierPrime,
                            fontSize = 13.sp,
                            color = tinta,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ficha2, RoundedCornerShape(4.dp))
                                    .border(1.dp, CorBordaCard, RoundedCornerShape(4.dp))
                                    .clickable { onMenuExpandidoChange(true) }
                                    .padding(horizontal = 12.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiState.categoriaSelecionada,
                                    fontFamily = CourierPrime,
                                    fontSize = 14.sp,
                                    color = tinta
                                )
                                IconeSetaBaixo(tint = tinta)
                            }

                            DropdownMenu(
                                expanded = uiState.menuCategoriaExpandido,
                                onDismissRequest = { onMenuExpandidoChange(false) },
                                modifier = Modifier.background(ficha2)
                            ) {
                                uiState.categoriasDisponiveis.forEach { cat ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = cat,
                                                fontFamily = CourierPrime,
                                                color = tinta
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
                            fontFamily = CourierPrime,
                            fontSize = 13.sp,
                            color = tinta,
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
                                        .background(if (selecionado) CorBadgeEscuro else ficha2)
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
                                        fontFamily = CourierPrime,
                                        fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = if (selecionado) Color.White else tinta
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão "Salvar caso"
            Button(
                onClick = onSalvarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = acao,
                    contentColor = sobreAcao
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Salvar caso",
                    fontFamily = CourierPrime,
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

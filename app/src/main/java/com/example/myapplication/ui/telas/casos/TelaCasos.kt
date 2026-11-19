package com.example.myapplication.ui.telas.casos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.model.enums.StatusCaso
import com.example.myapplication.ui.componentes.Clipe
import com.example.myapplication.ui.componentes.FitaDificuldade
import com.example.myapplication.ui.componentes.NavegacaoRodapeJogador
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CorTextoVermelho
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.abaInativa
import com.example.myapplication.ui.theme.carimbo
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.ficha2
import com.example.myapplication.ui.theme.linha
import com.example.myapplication.ui.theme.tinta

@Composable
fun TelaCasos(
    onItemNavegacaoClick: (String) -> Unit = {},
    onCasoClick: (Criptograma) -> Unit = {},
    viewModel: CasosViewModel = viewModel(
        factory = CasosViewModelFactory(
            UsuarioRepository(AppDatabase.getDatabase(LocalContext.current).usuarioDAO()),
            PartidaRepository(AppDatabase.getDatabase(LocalContext.current).partidaDAO()),
            CriptogramaRepository(AppDatabase.getDatabase(LocalContext.current).criptogramaDAO())
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fundoQuadriculado(),
        containerColor = Color.Transparent,
        topBar = { BarraSuperiorCasos(quantidadeCasos = uiState.quantidadeCasos) },
        bottomBar = {
            NavegacaoRodapeJogador(
                itemSelecionado = uiState.itemNavegacaoSelecionado,
                onItemClick = onItemNavegacaoClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp)
        ) {
            uiState.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    fontFamily = CourierPrime,
                    fontSize = 12.sp,
                    color = CorTextoVermelho,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            AbasDificuldade(
                dificuldadeSelecionada = uiState.dificuldadeSelecionada,
                onDificuldadeClick = viewModel::onDificuldadeSelecionada
            )
            PainelCasos(
                uiState = uiState,
                onCategoriaClick = viewModel::onCategoriaSelecionada,
                onCasoClick = onCasoClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BarraSuperiorCasos(quantidadeCasos: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Casos",
            fontFamily = CourierPrime,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            color = tinta
        )

        Text(
            text = "$quantidadeCasos ${if (quantidadeCasos == 1) "caso" else "casos"}",
            fontFamily = CourierPrime,
            fontSize = 16.sp,
            color = tinta
        )
    }
}

@Composable
fun AbasDificuldade(
    dificuldadeSelecionada: Dificuldade?,
    onDificuldadeClick: (Dificuldade?) -> Unit
) {
    val abas = listOf<Pair<String, Dificuldade?>>(
        "Todos" to null,
        "Fácil" to Dificuldade.FACIL,
        "Médio" to Dificuldade.MEDIO,
        "Difícil" to Dificuldade.DIFICIL
    )

    Row(modifier = Modifier.zIndex(1f)) {
        abas.forEach { (rotulo, dificuldade) ->
            AbaDificuldade(
                rotulo = rotulo,
                selecionada = dificuldade == dificuldadeSelecionada,
                onClick = { onDificuldadeClick(dificuldade) }
            )
        }
    }
}

@Composable
fun AbaDificuldade(
    rotulo: String,
    selecionada: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset(y = if (selecionada) 1.dp else 0.dp)
            .background(if (selecionada) ficha else abaInativa)
            .drawBehind {
                val espessura = 1.dp.toPx()
                drawLine(linha, Offset(0f, 0f), Offset(size.width, 0f), espessura)
                drawLine(linha, Offset(0f, 0f), Offset(0f, size.height), espessura)
                drawLine(linha, Offset(size.width, 0f), Offset(size.width, size.height), espessura)
                if (!selecionada) {
                    drawLine(linha, Offset(0f, size.height), Offset(size.width, size.height), espessura)
                }
            }
            .clickable(onClick = onClick)
            .heightIn(min = 40.dp)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rotulo,
            color = tinta,
            fontFamily = CourierPrime,
            fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PainelCasos(
    uiState: CasosUiState,
    onCategoriaClick: (String?) -> Unit,
    onCasoClick: (Criptograma) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ficha)
            .border(1.dp, linha)
    ) {
        SeletorCategoria(
            categoriaSelecionada = uiState.categoriaSelecionada,
            categorias = uiState.categorias,
            onCategoriaClick = onCategoriaClick
        )
        ListaCasos(
            uiState = uiState,
            onCasoClick = onCasoClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SeletorCategoria(
    categoriaSelecionada: String?,
    categorias: List<String>,
    onCategoriaClick: (String?) -> Unit
) {
    var menuAberto by remember { mutableStateOf(false) }

    Column {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { menuAberto = true }
                    .heightIn(min = 48.dp)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categoria: ${categoriaSelecionada ?: "Todas"}",
                    color = tinta,
                    fontFamily = CourierPrime,
                    fontSize = 14.sp
                )
                Text(
                    text = "▾",
                    color = tinta,
                    fontFamily = CourierPrime,
                    fontSize = 14.sp
                )
            }

            DropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false },
                modifier = Modifier.background(ficha2)
            ) {
                DropdownMenuItem(
                    text = { TextoOpcaoCategoria("Todas") },
                    onClick = {
                        onCategoriaClick(null)
                        menuAberto = false
                    }
                )
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { TextoOpcaoCategoria(categoria) },
                        onClick = {
                            onCategoriaClick(categoria)
                            menuAberto = false
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(linha)
        )
    }
}

@Composable
fun TextoOpcaoCategoria(texto: String) {
    Text(
        text = texto,
        color = tinta,
        fontFamily = CourierPrime,
        fontSize = 14.sp
    )
}

@Composable
fun ListaCasos(
    uiState: CasosUiState,
    onCasoClick: (Criptograma) -> Unit,
    modifier: Modifier = Modifier
) {
    val casos = uiState.criptogramasFiltrados

    if (casos.isEmpty()) {
        ListaCasosVazia(modifier)
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 18.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items = casos, key = { _, criptograma -> criptograma.id }) { indice, criptograma ->
            Box {
                ItemCaso(
                    numero = criptograma.id,
                    totalLetras = uiState.totalLetras(criptograma),
                    totalPalavras = uiState.totalPalavras(criptograma),
                    dificuldade = criptograma.dificuldade,
                    status = uiState.statusDoCaso(criptograma),
                    tempo = uiState.tempoFormatadoDoCaso(criptograma),
                    pontos = uiState.pontosDoCaso(criptograma),
                    onClick = { onCasoClick(criptograma) }
                )
                if (indice == 0) {
                    Clipe(
                        Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 8.dp, y = (-14).dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ListaCasosVazia(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Nenhum caso encontrado",
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ItemCaso(
    numero: Int,
    totalLetras: Int,
    totalPalavras: Int,
    dificuldade: Dificuldade,
    status: StatusCaso,
    tempo: String,
    pontos: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ficha2)
            .border(1.dp, tinta)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Nº ${numero.toString().padStart(4, '0')}",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "$totalLetras letras · $totalPalavras palavras",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(8.dp))
            FitaDificuldade(dificuldade)
        }
        StatusDoCaso(status = status, tempo = tempo, pontos = pontos)
    }
}

@Composable
fun StatusDoCaso(
    status: StatusCaso,
    tempo: String,
    pontos: Int
) {
    Column(horizontalAlignment = Alignment.End) {
        when (status) {
            StatusCaso.NOVO -> {
                TextoStatus("Novo")
            }
            StatusCaso.EM_ANDAMENTO -> {
                TextoStatus("Em andamento")
                TextoStatus(tempo)
            }
            StatusCaso.DECIFRADO -> {
                CarimboDecifrado()
                Spacer(Modifier.height(6.dp))
                TextoStatus("$pontos pts")
            }
        }
    }
}

@Composable
fun TextoStatus(texto: String) {
    Text(
        text = texto,
        color = tinta,
        fontFamily = CourierPrime,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}

@Composable
fun CarimboDecifrado() {
    Text(
        text = "Decifrado",
        modifier = Modifier
            .rotate(-6f)
            .border(2.dp, carimbo)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        color = carimbo,
        fontFamily = CourierPrime,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp
    )
}
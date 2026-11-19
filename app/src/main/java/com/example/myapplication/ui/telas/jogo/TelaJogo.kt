package com.example.myapplication.ui.telas.jogo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.model.CelulaCriptograma
import com.example.myapplication.model.PalavraCriptograma
import com.example.myapplication.model.enums.StatusCaso
import com.example.myapplication.ui.componentes.Clipe
import com.example.myapplication.ui.componentes.EtiquetaEvidencia
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CorTextoVermelho
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.acao
import com.example.myapplication.ui.theme.carimbo
import com.example.myapplication.ui.theme.destaque
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.ficha2
import com.example.myapplication.ui.theme.linha
import com.example.myapplication.ui.theme.mesa
import com.example.myapplication.ui.theme.polaroid
import com.example.myapplication.ui.theme.polaroidFundo
import com.example.myapplication.ui.theme.silhueta
import com.example.myapplication.ui.theme.sobreFita
import com.example.myapplication.ui.theme.tarja
import com.example.myapplication.ui.theme.teclaUsada
import com.example.myapplication.ui.theme.tinta
import kotlinx.coroutines.delay

@Composable
fun TelaJogo(
    criptogramaId: Int? = null,
    onPausarClick: () -> Unit = {},
    onCasoDecifrado: (Int) -> Unit = {},
    viewModel: JogoViewModel = viewModel(
        factory = JogoViewModelFactory(
            UsuarioRepository(AppDatabase.getDatabase(LocalContext.current).usuarioDAO()),
            PartidaRepository(AppDatabase.getDatabase(LocalContext.current).partidaDAO()),
            CriptogramaRepository(AppDatabase.getDatabase(LocalContext.current).criptogramaDAO()),
            criptogramaId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val criptograma = uiState.criptograma

    BackHandler {
        viewModel.onPausarClick(onPausarClick)
    }

    LaunchedEffect(uiState.partidaConcluidaId) {
        val partidaId = uiState.partidaConcluidaId ?: return@LaunchedEffect
        delay(800)
        onCasoDecifrado(partidaId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fundoQuadriculado(),
        containerColor = Color.Transparent,
        topBar = {
            BarraSuperiorJogo(
                tempo = uiState.tempoFormatado,
                erros = uiState.erros,
                onPausarClick = { viewModel.onPausarClick(onPausarClick) }
            )
        },
        bottomBar = {
            PainelInferiorJogo(
                linhasTeclado = uiState.linhasTeclado,
                teclasUsadas = uiState.teclasUsadas,
                habilitado = uiState.podeInteragir,
                onRevelarLetraClick = viewModel::onRevelarLetraClick,
                onLetraClick = viewModel::onLetraDigitada,
                onApagarClick = viewModel::onApagarClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp)
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

            if (criptograma == null) {
                if (uiState.mensagemErro == null) {
                    MensagemCarregando()
                }
            } else {
                CardCaso(
                    criptograma = criptograma,
                    status = uiState.statusCaso,
                    palavras = uiState.palavras,
                    autor = uiState.autor,
                    autorRevelado = uiState.autorRevelado,
                    onCelulaClick = viewModel::onCelulaClick
                )
            }
        }
    }
}

@Composable
fun BarraSuperiorJogo(
    tempo: String,
    erros: Int,
    onPausarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotaoPausar(onClick = onPausarClick)
        Text(
            text = "$tempo · Erros $erros",
            fontFamily = CourierPrime,
            fontSize = 16.sp,
            color = tinta
        )
    }
}

@Composable
fun BotaoPausar(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(mesa)
            .border(1.dp, tinta)
            .clickable(onClick = onClick)
            .heightIn(min = 40.dp)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pausar",
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CardCaso(
    criptograma: Criptograma,
    status: StatusCaso,
    palavras: List<PalavraCriptograma>,
    autor: String,
    autorRevelado: Boolean,
    onCelulaClick: (Char) -> Unit
) {
    Box(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ficha)
                .border(1.dp, linha)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CabecalhoCaso(criptograma = criptograma, status = status)
            GradeCriptograma(palavras = palavras, onCelulaClick = onCelulaClick)
            SecaoAutor(autor = autor, revelado = autorRevelado)
        }
        Clipe(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
        )
    }
}

@Composable
fun CabecalhoCaso(criptograma: Criptograma, status: StatusCaso) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EtiquetaEvidencia(
            numero = criptograma.id,
            dificuldade = criptograma.dificuldade,
            categoria = criptograma.categoria
        )
        CarimboStatus(status = status)
    }
}

@Composable
fun CarimboStatus(status: StatusCaso) {
    val texto = when (status) {
        StatusCaso.NOVO -> "Novo caso"
        StatusCaso.EM_ANDAMENTO -> "Em análise"
        StatusCaso.DECIFRADO -> "Decifrado"
    }
    Text(
        text = texto,
        modifier = Modifier
            .rotate(-4f)
            .border(2.dp, carimbo)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = carimbo,
        fontFamily = CourierPrime,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GradeCriptograma(
    palavras: List<PalavraCriptograma>,
    onCelulaClick: (Char) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        palavras.forEach { palavra ->
            PalavraNaGrade(palavra = palavra, onCelulaClick = onCelulaClick)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PalavraNaGrade(
    palavra: PalavraCriptograma,
    onCelulaClick: (Char) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        palavra.celulas.forEach { celula ->
            if (celula.ehLetra) {
                CelulaLetra(
                    celula = celula,
                    onClick = { celula.letraCifrada?.let(onCelulaClick) }
                )
            } else {
                CelulaPontuacao(caractere = celula.caractere)
            }
        }
    }
}

@Composable
fun CelulaLetra(celula: CelulaCriptograma, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(28.dp)
            .background(if (celula.destacada) destaque else ficha2)
            .border(if (celula.errada) 2.dp else 1.dp, tinta)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = celula.letraCifrada?.toString() ?: "",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 12.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(linha)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = celula.palpite?.toString() ?: "",
                color = tinta,
                fontFamily = CourierPrime,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            if (celula.errada) {
                MarcaErro()
            }
        }
    }
}

@Composable
fun MarcaErro() {
    Canvas(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        val espessura = 2.dp.toPx()
        drawLine(tinta, Offset(0f, 0f), Offset(size.width, size.height), espessura, StrokeCap.Round)
        drawLine(tinta, Offset(size.width, 0f), Offset(0f, size.height), espessura, StrokeCap.Round)
    }
}

@Composable
fun CelulaPontuacao(caractere: Char) {
    Box(
        modifier = Modifier
            .width(12.dp)
            .height(49.dp)
            .padding(bottom = 4.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = caractere.toString(),
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 18.sp
        )
    }
}

@Composable
fun MensagemCarregando() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Carregando caso...",
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SecaoAutor(autor: String, revelado: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FotoAutor(inicial = if (revelado) autor.take(1).uppercase() else "?")
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "Autor",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(8.dp))
            TarjaAutor(autor = autor, revelado = revelado)
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (revelado) "Autor revelado" else "Revelado ao decifrar",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun FotoAutor(inicial: String) {
    Box(
        modifier = Modifier
            .rotate(-4f)
            .size(width = 68.dp, height = 76.dp)
            .background(polaroid)
            .border(1.dp, linha)
            .padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RectangleShape)
                .background(polaroidFundo),
            contentAlignment = Alignment.TopCenter
        ) {
            Canvas(Modifier.fillMaxSize()) {
                drawCircle(
                    color = silhueta,
                    radius = size.width * 0.38f,
                    center = Offset(size.width / 2, size.height)
                )
            }
            Text(
                text = inicial,
                modifier = Modifier.padding(top = 6.dp),
                color = polaroid,
                fontFamily = CourierPrime,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun TarjaAutor(autor: String, revelado: Boolean) {
    Box(
        modifier = Modifier
            .widthIn(min = 128.dp)
            .height(20.dp)
            .background(tarja)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (revelado) {
            Text(
                text = autor,
                color = sobreFita,
                fontFamily = CourierPrime,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PainelInferiorJogo(
    linhasTeclado: List<String>,
    teclasUsadas: Set<Char>,
    habilitado: Boolean,
    onRevelarLetraClick: () -> Unit,
    onLetraClick: (Char) -> Unit,
    onApagarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotoesDica(
            habilitado = habilitado,
            onRevelarLetraClick = onRevelarLetraClick,
        )
        TecladoVirtual(
            linhas = linhasTeclado,
            teclasUsadas = teclasUsadas,
            habilitado = habilitado,
            onLetraClick = onLetraClick,
            onApagarClick = onApagarClick
        )
    }
}

@Composable
fun BotoesDica(
    habilitado: Boolean,
    onRevelarLetraClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BotaoDica(
            texto = "Revelar letra · -${JogoUiState.CUSTO_REVELAR_LETRA}",
            icone = Icons.Filled.Search,
            habilitado = habilitado,
            onClick = onRevelarLetraClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BotaoDica(
    texto: String,
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icone: ImageVector? = null
) {
    Row(
        modifier = modifier
            .alpha(if (habilitado) 1f else 0.5f)
            .background(mesa)
            .border(1.dp, tinta)
            .clickable(enabled = habilitado, onClick = onClick)
            .heightIn(min = 48.dp)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icone != null) {
            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = tinta,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = texto,
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TecladoVirtual(
    linhas: List<String>,
    teclasUsadas: Set<Char>,
    habilitado: Boolean,
    onLetraClick: (Char) -> Unit,
    onApagarClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        linhas.forEachIndexed { indice, linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                linha.forEach { letra ->
                    TeclaLetra(
                        letra = letra,
                        usada = letra in teclasUsadas,
                        habilitada = habilitado,
                        onClick = { onLetraClick(letra) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (indice == linhas.lastIndex) {
                    TeclaApagar(
                        habilitada = habilitado,
                        onClick = onApagarClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TeclaLetra(
    letra: Char,
    usada: Boolean,
    habilitada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(if (usada) teclaUsada else ficha2)
            .border(1.dp, linha)
            .clickable(enabled = habilitada, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra.toString(),
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 18.sp
        )
        if (usada) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 3.dp, end = 3.dp)
                    .size(5.dp)
                    .background(acao)
            )
        }
    }
}

@Composable
fun TeclaApagar(
    habilitada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(ficha2)
            .border(1.dp, linha)
            .clickable(enabled = habilitada, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "←",
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 18.sp
        )
    }
}
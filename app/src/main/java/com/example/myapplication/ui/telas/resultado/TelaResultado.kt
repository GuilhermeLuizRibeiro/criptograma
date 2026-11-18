package com.example.myapplication.ui.telas.resultado

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
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
import com.example.myapplication.ui.componentes.EtiquetaEvidencia
import com.example.myapplication.ui.componentes.LinhaPontilhada
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.acao
import com.example.myapplication.ui.theme.carimbo
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.linha
import com.example.myapplication.ui.theme.polaroid
import com.example.myapplication.ui.theme.polaroidFundo
import com.example.myapplication.ui.theme.silhueta
import com.example.myapplication.ui.theme.sobreAcao
import com.example.myapplication.ui.theme.tinta

@Composable
fun TelaResultado(
    partidaId: Int,
    onVoltarInicioClick: () -> Unit = {},
    viewModel: ResultadoViewModel = viewModel(
        factory = ResultadoViewModelFactory(
            UsuarioRepository(AppDatabase.getDatabase(LocalContext.current).usuarioDAO()),
            PartidaRepository(AppDatabase.getDatabase(LocalContext.current).partidaDAO()),
            CriptogramaRepository(AppDatabase.getDatabase(LocalContext.current).criptogramaDAO()),
            partidaId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val criptograma = uiState.criptograma

    BackHandler(onBack = onVoltarInicioClick)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fundoQuadriculado(),
        containerColor = Color.Transparent,
        topBar = { BarraSuperiorResultado(tempo = uiState.tempoFormatado) },
        bottomBar = { BotaoVoltarInicio(onClick = onVoltarInicioClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (criptograma == null) {
                MensagemCarregandoResultado()
            } else {
                CardCasoDecifrado(
                    criptograma = criptograma,
                    frase = uiState.fraseEntreAspas,
                    autor = uiState.autor,
                    nomeUsuario = uiState.nomeUsuario,
                    data = uiState.dataFormatada
                )
                CardEstatisticas(
                    tempo = uiState.tempoFormatado,
                    erros = uiState.erros,
                    dicas = uiState.totalDicas,
                    pontuacao = uiState.pontuacaoFormatada
                )
            }
        }
    }
}

@Composable
fun BarraSuperiorResultado(tempo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Caso encerrado",
            fontFamily = CourierPrime,
            fontSize = 16.sp,
            color = tinta
        )
        Text(
            text = tempo,
            fontFamily = CourierPrime,
            fontSize = 16.sp,
            color = tinta
        )
    }
}

@Composable
fun CardCasoDecifrado(
    criptograma: Criptograma,
    frase: String,
    autor: String,
    nomeUsuario: String,
    data: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ficha)
            .border(1.dp, linha)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CabecalhoResultado(criptograma = criptograma)
        FraseDecifrada(frase = frase)
        AutorDaFrase(autor = autor)
        RodapeDecifradoPor(nomeUsuario = nomeUsuario, data = data)
    }
}

@Composable
fun CabecalhoResultado(criptograma: Criptograma) {
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
        CarimboResultado()
    }
}

@Composable
fun CarimboResultado() {
    Text(
        text = "Decifrado",
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

@Composable
fun FraseDecifrada(frase: String) {
    Text(
        text = frase,
        color = tinta,
        fontFamily = CourierPrime,
        fontSize = 22.sp,
        lineHeight = 32.sp
    )
}

@Composable
fun AutorDaFrase(autor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FotoAutorResultado()
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = "Autor",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Text(
                text = autor,
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun FotoAutorResultado() {
    Box(
        modifier = Modifier
            .rotate(-4f)
            .size(width = 68.dp, height = 76.dp)
            .background(polaroid)
            .border(1.dp, linha)
            .padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 14.dp)
    ) {
        Canvas(
            Modifier
                .fillMaxSize()
                .clip(RectangleShape)
                .background(polaroidFundo)
        ) {
            drawCircle(
                color = silhueta,
                radius = size.width * 0.2f,
                center = Offset(size.width / 2, size.height * 0.36f)
            )
            drawCircle(
                color = silhueta,
                radius = size.width * 0.38f,
                center = Offset(size.width / 2, size.height * 1.02f)
            )
        }
    }
}

@Composable
fun RodapeDecifradoPor(nomeUsuario: String, data: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp)
        ) {
            Text(
                text = nomeUsuario,
                color = tinta,
                fontFamily = CourierPrime,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                lineHeight = 26.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(tinta)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Decifrado por",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = data,
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Data",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun CardEstatisticas(
    tempo: String,
    erros: Int,
    dicas: Int,
    pontuacao: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ficha)
            .border(1.dp, linha)
            .padding(14.dp)
    ) {
        LinhaPontilhada("Tempo", tempo)
        LinhaPontilhada("Erros", erros.toString())
        LinhaPontilhada("Dicas", dicas.toString())
        DivisorEstatisticas()
        LinhaPontilhada("Pontuação", pontuacao)
    }
}

@Composable
fun DivisorEstatisticas() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(1.dp)
            .background(tinta)
    )
}

@Composable
fun MensagemCarregandoResultado() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Carregando resultado...",
            color = tinta,
            fontFamily = CourierPrime,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BotaoVoltarInicio(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .heightIn(min = 52.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = acao,
            contentColor = sobreAcao
        )
    ) {
        Text(
            text = "Voltar ao início",
            fontFamily = CourierPrime,
            fontSize = 16.sp
        )
    }
}
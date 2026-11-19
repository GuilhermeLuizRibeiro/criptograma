package com.example.myapplication.ui.telas.inicio

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.database.AppDatabase
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.repository.CriptogramaRepository
import com.example.myapplication.data.repository.PartidaRepository
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.ui.componentes.Clipe
import com.example.myapplication.ui.componentes.EtiquetaEvidencia
import com.example.myapplication.ui.componentes.FitaDificuldade
import com.example.myapplication.ui.componentes.LinhaPontilhada
import com.example.myapplication.ui.componentes.NavegacaoRodapeAdmin
import com.example.myapplication.ui.componentes.NavegacaoRodapeJogador
import com.example.myapplication.ui.componentes.fundoQuadriculado
import com.example.myapplication.ui.theme.CorTextoVermelho
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.acao
import com.example.myapplication.ui.theme.ficha
import com.example.myapplication.ui.theme.linha
import com.example.myapplication.ui.theme.sobreAcao
import com.example.myapplication.ui.theme.tinta

@Composable
fun TelaInicio(
    onItemNavegacaoClick: (String) -> Unit = {},
    onContinuarClick: () -> Unit = {},
    viewModel: InicioViewModel = viewModel(
        factory = InicioViewModelFactory(
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
        topBar = { BarraSuperior(nome = uiState.nomeExibicao) },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            uiState.mensagemErro?.let { mensagem ->
                Text(
                    text = mensagem,
                    fontFamily = CourierPrime,
                    fontSize = 12.sp,
                    color = CorTextoVermelho
                )
            }

            uiState.criptogramaEmAndamento?.let { criptograma ->
                CardContinuarCaso(
                    criptograma,
                    uiState.totalLetras,
                    uiState.tempoFormatado,
                    onContinuarClick = onContinuarClick
                )
            }
            CardSeuArquivo(
                uiState.casosResolvidos,
                uiState.pontos
            )
            CardHistorico(uiState)
        }
    }
}

@Composable
fun CardContinuarCaso(
    criptograma: Criptograma,
    totalLetras: Int,
    tempo: String,
    onContinuarClick: () -> Unit
) {
    Surface() {
        Box(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ficha)
                    .border(1.dp, linha)
                    .padding(14.dp),
            ) {
                Text(
                    text = "Continuar caso",
                    color = tinta,
                    fontFamily = CourierPrime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(12.dp))
                EtiquetaEvidencia(
                    numero = criptograma.id,
                    dificuldade = criptograma.dificuldade,
                    categoria = criptograma.categoria
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "$totalLetras letras · $tempo",
                    color = tinta,
                    fontFamily = CourierPrime,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onContinuarClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = acao,
                        contentColor = sobreAcao
                    )
                ) {
                    Text(
                        text = "Continuar",
                        fontFamily = CourierPrime,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Clipe(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-16).dp, y = (-12).dp)
            )
        }
    }
}

@Composable
fun CardSeuArquivo(casosResolvidos: Int, pontos: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ficha)
            .border(1.dp, linha)
            .padding(14.dp),
    ) {
        Text(
            text = "Seu arquivo",
            color = tinta,
            fontFamily = CourierPrime,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
        Spacer(Modifier.height(8.dp))
        LinhaPontilhada("Casos resolvidos", casosResolvidos.toString())
        LinhaPontilhada("Pontos", "%,d".format(pontos))
    }
}

@Composable
fun CardHistorico(uiState: InicioUiState) {
    val historico = uiState.historico

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ficha)
            .border(1.dp, linha)
            .padding(14.dp),
    ) {
        Text(
            text = "Histórico",
            color = tinta,
            fontFamily = CourierPrime,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
        Spacer(Modifier.height(8.dp))
        if (historico.isEmpty()) {
            HistoricoVazio()
        } else {
            historico.forEachIndexed { indice, partida ->
                if (indice > 0) {
                    DivisorHistorico()
                }
                ItemHistorico(
                    titulo = uiState.tituloDaPartida(partida),
                    dificuldade = uiState.criptogramaDaPartida(partida)?.dificuldade,
                    data = uiState.dataFormatadaDaPartida(partida),
                    tempo = uiState.tempoFormatadoDaPartida(partida),
                    pontos = partida.pontuacao
                )
            }
        }
    }
}

@Composable
fun ItemHistorico(
    titulo: String,
    dificuldade: Dificuldade?,
    data: String,
    tempo: String,
    pontos: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = titulo,
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            if (dificuldade != null) {
                Spacer(Modifier.height(6.dp))
                FitaDificuldade(dificuldade)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = data,
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Text(
                text = "$tempo · $pontos pts",
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun DivisorHistorico() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(linha)
    )
}

@Composable
fun HistoricoVazio() {
    Text(
        text = "Nenhum caso resolvido ainda",
        color = tinta,
        fontFamily = CourierPrime,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
}

@Composable
fun BarraSuperior(nome: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Início",
            fontFamily = CourierPrime,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            color = tinta
        )

        Text(
            text = nome,
            fontFamily = CourierPrime,
            fontSize = 16.sp,
            color = tinta
        )
    }
}
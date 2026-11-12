package com.example.myapplication.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorBotaoRoxo
import com.example.myapplication.ui.theme.CorFundoCard
import com.example.myapplication.ui.theme.CorTextoEscuro

@Composable
fun NavegacaoRodapeJogador(
    itemSelecionado: String,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {},
) {
    Surface(
        color = CorFundoCard,
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = CorBordaCard,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val itens = listOf("Início", "Casos", "Ranking", "Perfil")

            itens.forEach { titulo ->
                val ativo = titulo == itemSelecionado
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onItemClick(titulo) }
                ) {
                    when (titulo) {
                        "Início" -> InicioIcon(if (ativo) CorBotaoRoxo else CorTextoEscuro)
                        "Casos" -> CasosIcon(if (ativo) CorBotaoRoxo else CorTextoEscuro)
                        "Ranking" -> RankingIcon(if (ativo) CorBotaoRoxo else CorTextoEscuro)
                        "Perfil" -> PerfilIcon(if (ativo) CorBotaoRoxo else CorTextoEscuro)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = titulo,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = if (ativo) FontWeight.Bold else FontWeight.Normal,
                        color = if (ativo) CorBotaoRoxo else CorTextoEscuro
                    )
                }
            }
        }
    }
}

@Composable
private fun InicioIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val strokeWidth = 1.8.dp.toPx()
        val path = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.15f)
            lineTo(size.width * 0.9f, size.height * 0.5f)
            lineTo(size.width * 0.78f, size.height * 0.5f)
            lineTo(size.width * 0.78f, size.height * 0.88f)
            lineTo(size.width * 0.22f, size.height * 0.88f)
            lineTo(size.width * 0.22f, size.height * 0.5f)
            lineTo(size.width * 0.1f, size.height * 0.5f)
            close()
        }
        drawPath(path, color = tint, style = Stroke(width = strokeWidth))
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
        drawPath(path, color = tint, style = Stroke(width = 1.8.dp.toPx()))
    }
}

@Composable
private fun RankingIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val barWidth = 4.dp.toPx()
        val gap = 3.dp.toPx()
        val startX = (size.width - (3 * barWidth + 2 * gap)) / 2

        drawRoundRect(
            color = tint,
            topLeft = Offset(startX, size.height * 0.55f),
            size = Size(barWidth, size.height * 0.35f),
            cornerRadius = CornerRadius(2f)
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(startX + barWidth + gap, size.height * 0.25f),
            size = Size(barWidth, size.height * 0.65f),
            cornerRadius = CornerRadius(2f)
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(startX + (barWidth + gap) * 2, size.height * 0.4f),
            size = Size(barWidth, size.height * 0.5f),
            cornerRadius = CornerRadius(2f)
        )
    }
}

@Composable
private fun PerfilIcon(tint: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val strokeWidth = 1.8.dp.toPx()
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

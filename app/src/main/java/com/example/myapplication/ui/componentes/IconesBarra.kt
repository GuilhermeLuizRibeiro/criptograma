package com.example.myapplication.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun InicioIcon(tint: Color = LocalContentColor.current) {
    Canvas(modifier = Modifier.size(20.dp)) {
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
        drawPath(path, color = tint, style = Stroke(width = 1.8.dp.toPx()))
    }
}

@Composable
fun CasosIcon(tint: Color = LocalContentColor.current) {
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
fun PerfilIcon(tint: Color = LocalContentColor.current) {
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

@Composable
fun PainelIcon(tint: Color = LocalContentColor.current) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val gap = 3.dp.toPx()
        val half = (size.width - gap) / 2
        val raio = CornerRadius(4f)
        drawRoundRect(tint, Offset(0f, 0f), Size(half, half), raio)
        drawRoundRect(tint, Offset(half + gap, 0f), Size(half, half), raio)
        drawRoundRect(tint, Offset(0f, half + gap), Size(half, half), raio)
        drawRoundRect(tint, Offset(half + gap, half + gap), Size(half, half), raio)
    }
}
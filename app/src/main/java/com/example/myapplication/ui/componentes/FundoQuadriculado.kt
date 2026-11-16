package com.example.myapplication.ui.componentes

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.mesa
import com.example.myapplication.ui.theme.quadriculado

fun Modifier.fundoQuadriculado(tamanho: Dp = 16.dp, espessura: Dp = 0.5.dp): Modifier = this.drawWithCache {
    val passo = tamanho.toPx()
    val traco = espessura.toPx()
    onDrawBehind {
        drawRect(mesa)
        var x = 0f
        while (x <= size.width) {
            drawLine(quadriculado, Offset(x, 0f), Offset(x, size.height), traco)
            x += passo
        }
        var y = 0f
        while (y <= size.height) {
            drawLine(quadriculado, Offset(0f, y), Offset(size.width, y), traco)
            y += passo
        }
    }
}
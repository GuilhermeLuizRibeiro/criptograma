package com.example.myapplication.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.silhueta

@Composable
fun Clipe(modifier: Modifier = Modifier) {
    Canvas(modifier.size(width = 14.dp, height = 32.dp)) {
        val u = 1.dp.toPx()
        val path = Path().apply {
            moveTo(1 * u, 6 * u)
            lineTo(1 * u, 25 * u)
            arcTo(Rect(1 * u, 19 * u, 13 * u, 31 * u), 180f, -180f, false)
            lineTo(13 * u, 8 * u)
            arcTo(Rect(5 * u, 4 * u, 13 * u, 12 * u), 0f, -180f, false)
            lineTo(5 * u, 22 * u)
            arcTo(Rect(5 * u, 20 * u, 9 * u, 24 * u), 180f, -180f, false)
            lineTo(9 * u, 10 * u)
        }
        drawPath(
            path = path,
            color = silhueta,
            style = Stroke(
                width = 1.8.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
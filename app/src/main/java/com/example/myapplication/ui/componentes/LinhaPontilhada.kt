package com.example.myapplication.ui.componentes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.tinta

@Composable
fun LinhaPontilhada(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(rotulo, color = tinta, fontFamily = CourierPrime, fontSize = 16.sp, lineHeight = 24.sp)
        Box(
            Modifier
                .weight(1f)
                .padding(start = 6.dp, end = 6.dp, bottom = 7.dp)
                .height(1.dp)
                .drawBehind {
                    drawLine(
                        color = tinta,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(1.dp.toPx(), 3.dp.toPx())
                        )
                    )
                }
        )
        Text(valor, color = tinta, fontFamily = CourierPrime, fontSize = 16.sp, lineHeight = 24.sp)
    }
}
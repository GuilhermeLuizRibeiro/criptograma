package com.example.myapplication.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.ficha2
import com.example.myapplication.ui.theme.mesa
import com.example.myapplication.ui.theme.tinta

@Composable
fun EtiquetaEvidencia(
    numero: Int,
    dificuldade: Dificuldade,
    modifier: Modifier = Modifier,
    categoria: String? = null,
) {
    Row(
        modifier = modifier
            .background(ficha2)
            .border(1.dp, tinta)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(8.dp)
                .background(mesa, CircleShape)
                .border(1.dp, tinta, CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Nº ${numero.toString().padStart(4, '0')}")
                    }
                    if (categoria != null) append(" · $categoria")
                },
                color = tinta,
                fontFamily = CourierPrime,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(Modifier.height(4.dp))
            FitaDificuldade(dificuldade)
        }
    }
}
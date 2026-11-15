package com.example.myapplication.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.enums.Dificuldade
import com.example.myapplication.ui.theme.CourierPrime
import com.example.myapplication.ui.theme.fita
import com.example.myapplication.ui.theme.sobreFita

@Composable
fun FitaDificuldade(
    dificuldade: Dificuldade,
    modifier: Modifier = Modifier,
) {
    val texto = when (dificuldade) {
        Dificuldade.FACIL -> "FÁCIL"
        Dificuldade.MEDIO -> "MÉDIO"
        Dificuldade.DIFICIL -> "DIFÍCIL"
    }
    Text(
        text = texto,
        modifier = modifier
            .background(fita)
            .padding(horizontal = 6.dp, vertical = 1.dp),
        color = sobreFita,
        fontFamily = CourierPrime,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.em
    )
}
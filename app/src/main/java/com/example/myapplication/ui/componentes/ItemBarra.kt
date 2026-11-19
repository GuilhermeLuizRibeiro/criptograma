package com.example.myapplication.ui.componentes

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CorBotaoRoxo
import com.example.myapplication.ui.theme.CorTextoEscuro
import com.example.myapplication.ui.theme.CourierPrime

@Composable
fun RowScope.ItemBarra(
    titulo: String,
    itemSelecionado: String,
    onItemClick: (String) -> Unit,
    icone: @Composable () -> Unit
) {
    NavigationBarItem(
        selected = titulo == itemSelecionado,
        onClick = { onItemClick(titulo) },
        icon = icone,
        label = { Text(titulo, fontFamily = CourierPrime, fontSize = 11.sp) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CorBotaoRoxo,
            selectedTextColor = CorBotaoRoxo,
            unselectedIconColor = CorTextoEscuro,
            unselectedTextColor = CorTextoEscuro,
            indicatorColor = Color.Transparent
        )
    )
}
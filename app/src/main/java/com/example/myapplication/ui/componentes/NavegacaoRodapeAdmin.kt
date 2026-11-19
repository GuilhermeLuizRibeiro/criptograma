package com.example.myapplication.ui.componentes

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.CorBordaCard
import com.example.myapplication.ui.theme.CorFundoCard

@Composable
fun NavegacaoRodapeAdmin(
    itemSelecionado: String,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit = {},
) {
    NavigationBar(
        containerColor = CorFundoCard,
        modifier = modifier.drawBehind {
            drawLine(CorBordaCard, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
        }
    ) {
        ItemBarra("Painel", itemSelecionado, onItemClick) { PainelIcon() }
        ItemBarra("Perfil", itemSelecionado, onItemClick) { PerfilIcon() }
    }
}
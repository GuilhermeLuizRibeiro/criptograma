package com.example.myapplication.ui.telas.inicio

import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.local.entity.Usuario
import com.example.myapplication.model.enums.Papel

data class InicioUiState(
    val usuarioId: Int? = null,
    val usuarios: List<Usuario> = emptyList(),
    val partidas: List<Partida> = emptyList(),
    val criptogramas: List<Criptograma> = emptyList(),
    val itemNavegacaoSelecionado: String = "Início",
    val carregando: Boolean = false
) {
    fun criptogramaDaPartida(partida: Partida): Criptograma? {
        return criptogramas.find { it.id == partida.criptogramaId }
    }

    val usuario: Usuario?
        get() = usuarios.find { it.id == usuarioId }

    val partidasDoUsuario: List<Partida>
        get() = partidas.filter { it.usuarioId == usuarioId }

    val casoEmAndamento: Partida?
        get() = partidasDoUsuario.firstOrNull { !it.concluida }

    val criptogramaEmAndamento: Criptograma?
        get() = casoEmAndamento?.let { criptogramaDaPartida(it) }

    val temCasoEmAndamento: Boolean
        get() = casoEmAndamento != null

    val totalLetras: Int
        get() = criptogramaEmAndamento?.frase?.count { it.isLetter() } ?: 0

    val tempoFormatado: String
        get() {
            val segundos = casoEmAndamento?.tempo ?: 0L
            return "%02d:%02d".format(segundos / 60, segundos % 60)
        }

    val nomeExibicao: String
        get() = usuario?.nome ?: "Jogador"

    val casosResolvidos: Int
        get() = usuario?.casosResolvidos ?: 0

    val pontos: Int
        get() = usuario?.pontos ?: 0

    val ranking: List<Usuario>
        get() = usuarios
            .filter { it.papel == Papel.USUARIO }
            .sortedByDescending { it.pontos }

    val posicaoRanking: Int
        get() = ranking.indexOfFirst { it.id == usuarioId } + 1
}
package com.example.myapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "partidas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Criptograma::class,
            parentColumns = ["id"],
            childColumns = ["criptogramaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId"), Index("criptogramaId")]
)
data class Partida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val criptogramaId: Int?,
    val tempo: Long = 0,
    val erros: Int = 0,
    val dicasUsadas: Int = 0,
    val originalRevelado: Boolean = false,
    val pontuacao: Int = 0,
    val concluida: Boolean = false,
    val estadoAtual: String,
    val iniciadaEm: Long,
    val concluidaEm: Long? = null
)
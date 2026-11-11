package com.example.myapplication.data.local.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.example.myapplication.model.enums.Dificuldade

@Entity(
    tableName = "criptogramas",
    indices = [Index(value = ["idExterno"], unique = true)]
)
data class Criptograma(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val frase: String,
    val autor: String,
    val categoria: String,
    val dificuldade: Dificuldade,
    val idExterno: String? = null
)
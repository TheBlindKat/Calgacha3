package com.example.calgacha.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gallinas")
data class Chicken(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val edad: Int,
    val raza: String,
    val descripcion: String,
    val imagenUri: String? = null
)
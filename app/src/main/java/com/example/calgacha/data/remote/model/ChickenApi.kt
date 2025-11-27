package com.example.calgacha.data.remote.model

data class ChickenApi(
    val id: String? = null,
    val nombre: String,
    val edad: Int,
    val raza: String,
    val descripcion: String,
    val imagenUri: String? = null
)

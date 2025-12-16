package com.example.calgacha.data.remote.model

import com.google.gson.annotations.SerializedName

data class ChickenApi(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("raza") val raza: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagenUri") val imagenUri: String?
)

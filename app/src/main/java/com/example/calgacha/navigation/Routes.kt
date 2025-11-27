package com.example.calgacha.navigation

object Routes {
    const val HOME = "Inicio"
    const val PROFILE = "Perfil"
    const val ADD = "Añadir"
    const val HISTORY = "Historial"
    const val HISTORY_DETAIL = "Detalles_Historial/{itemId}"

    const val DETAIL = "detail/{chickenId}"
    fun detailRoute(chickenId: Int) = "detail/$chickenId"
    fun historyDetailRoute(itemId: Int) = "Detalles_Historial/$itemId"
}
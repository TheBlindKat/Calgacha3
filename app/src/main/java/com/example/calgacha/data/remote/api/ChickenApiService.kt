package com.example.calgacha.data.remote

import androidx.compose.ui.graphics.vector.Path
import com.example.calgacha.data.remote.model.ChickenApi
import retrofit2.Response
import retrofit2.http.*

interface ChickenApiService {

    @GET("chickens")
    suspend fun getChickens(): Response<List<ChickenApi>>

    @POST("chickens")
    suspend fun createChicken(@Body chicken: ChickenApi): Response<ChickenApi>

    @PUT("chickens/{id}")
    suspend fun updateChicken(
        @Path("id") id: String,
        @Body chicken: ChickenApi
    ): Response<ChickenApi>

    @DELETE("chickens/{id}")
    suspend fun deleteChicken(@Path("id") id: String): Response<Unit>
}

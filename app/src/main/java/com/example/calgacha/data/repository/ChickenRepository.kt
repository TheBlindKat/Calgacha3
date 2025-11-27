package com.example.calgacha.data.repository

import com.example.calgacha.data.local.dao.ChickenDao
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.data.remote.model.ChickenApi
import com.example.calgacha.data.remote.ChickenApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChickenRepository(
    private val chickenDao: ChickenDao,
    private val api: ChickenApiService
) {
    //local
    fun getChickens(): Flow<List<Chicken>> =
        chickenDao.getAllChickens()

    suspend fun getChickenById(id: Int) =
        chickenDao.getChickenById(id)

    suspend fun insertChicken(chicken: Chicken) =
        chickenDao.insertChicken(chicken)

    suspend fun deleteChicken(chicken: Chicken) =
        chickenDao.deleteChicken(chicken)


    //api
    suspend fun getRemoteChickens(): List<ChickenApi>? = withContext(Dispatchers.IO) {
        val res = api.getChickens()
        if (res.isSuccessful) res.body() else null
    }

    suspend fun createRemoteChicken(chicken: ChickenApi): ChickenApi? = withContext(Dispatchers.IO) {
        val res = api.createChicken(chicken)
        if (res.isSuccessful) res.body() else null
    }

    suspend fun updateRemoteChicken(id: String, chicken: ChickenApi): ChickenApi? = withContext(Dispatchers.IO) {
        val res = api.updateChicken(id, chicken)
        if (res.isSuccessful) res.body() else null
    }

    suspend fun deleteRemoteChicken(id: String): Boolean = withContext(Dispatchers.IO) {
        api.deleteChicken(id).isSuccessful
    }

    //api con el room
    suspend fun getChickensFromApi(): List<Chicken> {
        val remote = getRemoteChickens() ?: return emptyList()

        return remote.map { api ->
            Chicken(
                id = 0,
                nombre = api.nombre,
                edad = api.edad,
                raza = api.raza,
                descripcion = api.descripcion,
                imagenUri = api.imagenUri
            )
        }
    }

    suspend fun syncFromApi() = withContext(Dispatchers.IO) {
        val remoteChickens = getRemoteChickens() ?: return@withContext

        remoteChickens.forEach { apiModel ->
            val localEntity = Chicken(
                id = 0,
                nombre = apiModel.nombre,
                edad = apiModel.edad,
                raza = apiModel.raza,
                descripcion = apiModel.descripcion,
                imagenUri = apiModel.imagenUri
            )
            chickenDao.insertChicken(localEntity)
        }
    }
}

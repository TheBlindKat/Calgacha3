package com.example.calgacha.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calgacha.data.local.model.Chicken
import kotlinx.coroutines.flow.Flow

@Dao
interface ChickenDao {
    @Insert
    suspend fun insertChicken(chicken: Chicken)
    @Query("SELECT * FROM Gallinas WHERE id = :id")
    suspend fun getChickenById(id: Int): Chicken?
    @Query("SELECT * FROM Gallinas")
    fun getAllChickens(): Flow<List<Chicken>>
    @Delete
    suspend fun deleteChicken(chicken: Chicken)


}
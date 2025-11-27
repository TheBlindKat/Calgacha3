package com.example.calgacha.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calgacha.data.local.dao.ChickenDao
import com.example.calgacha.data.local.model.Chicken

@Database(entities = [Chicken::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chickenDao(): ChickenDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chickens_db"
                )
                    .fallbackToDestructiveMigration()   //
                    .allowMainThreadQueries()
                    .build()                            //
                    .also { INSTANCE = it }
            }
        }
    }
}

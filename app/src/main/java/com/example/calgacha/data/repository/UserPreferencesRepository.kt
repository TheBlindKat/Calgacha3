package com.example.calgacha.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_EMAIL = stringPreferencesKey("Email del usuario")
    }

    val userEmail: Flow<String?> = context.dataStore.data
        .map {
            it[PreferencesKeys.USER_EMAIL]
        }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit {
            it[PreferencesKeys.USER_EMAIL] = email
        }
    }

    suspend fun clearUserEmail() {
        context.dataStore.edit {
            it.clear()
        }
    }
}

package com.example.calgacha.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calgacha.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserPreferencesRepository) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf<String?>(null)

    val userEmail: StateFlow<String?> = repository.userEmail
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun validate() {
        errorMessage.value = when {
            email.value.isBlank() -> "El email es obligatorio"
            !email.value.contains("@") -> "Formato inválido"
            password.value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> {
                viewModelScope.launch {
                    repository.saveUserEmail(email.value)
                }
                null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearUserEmail()
        }
    }
}

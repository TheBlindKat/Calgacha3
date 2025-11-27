package com.example.calgacha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calgacha.data.repository.ChickenRepository
import com.example.calgacha.data.repository.UserPreferencesRepository

class ViewModelFactory(
    private val chickenRepository: ChickenRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(chickenRepository) as T
        }

        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(chickenRepository) as T
        }

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userPreferencesRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

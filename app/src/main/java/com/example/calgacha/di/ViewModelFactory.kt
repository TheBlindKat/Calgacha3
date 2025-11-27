package com.example.calgacha.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calgacha.data.repository.ChickenRepository
import com.example.calgacha.data.repository.UserPreferencesRepository
import com.example.calgacha.ui.viewmodel.AddViewModel
import com.example.calgacha.ui.viewmodel.LoginViewModel
import com.example.calgacha.ui.viewmodel.MainViewModel

class ViewModelFactory(
    private val chickenRepository: ChickenRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(chickenRepository) as T

            modelClass.isAssignableFrom(AddViewModel::class.java) ->
                AddViewModel(chickenRepository) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(userPreferencesRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

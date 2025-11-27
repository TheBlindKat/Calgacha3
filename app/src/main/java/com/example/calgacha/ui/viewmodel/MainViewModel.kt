package com.example.calgacha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.data.repository.ChickenRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val chickenRepository: ChickenRepository) : ViewModel() {

    // LiveData con la lista mapeada de la API (ya como List<Chicken> - modelo local)
    val apiChickens = liveData {
        val data = chickenRepository.getChickensFromApi()
        emit(data)
    }

    // Gallinas locales (Room) ← ya tienes getChickens() que devuelve Flow<List<Chicken>>
    val chickens: StateFlow<List<Chicken>> =
        chickenRepository.getChickens()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    fun deleteChicken(chicken: Chicken) {
        viewModelScope.launch {
            chickenRepository.deleteChicken(chicken)
        }
    }

    suspend fun getChickenById(id: Int): Chicken? {
        return chickenRepository.getChickenById(id)
    }
}

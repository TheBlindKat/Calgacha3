package com.example.calgacha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.data.remote.model.ChickenApi
import com.example.calgacha.data.repository.ChickenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val chickenRepository: ChickenRepository) : ViewModel() {

    // ============================================
    // 🟢 LOCAL (ROOM)
    // ============================================

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

    // ⬇️ NUEVA FUNCIÓN: Actualizar gallina local
    fun updateChicken(chicken: Chicken) {
        viewModelScope.launch {
            chickenRepository.updateChicken(chicken)
        }
    }

    suspend fun getChickenById(id: Int): Chicken? {
        return chickenRepository.getChickenById(id)
    }

    // ============================================
    // 🟦 REMOTO (API)
    // ============================================

    private val _remoteChickens = MutableStateFlow<List<ChickenApi>>(emptyList())
    val remoteChickens: StateFlow<List<ChickenApi>> = _remoteChickens

    init {
        loadRemoteChickens()
    }

    fun loadRemoteChickens() {
        viewModelScope.launch {
            val chickens = chickenRepository.getRemoteChickens()
            if (chickens != null) {
                _remoteChickens.value = chickens
            }
        }
    }

    fun deleteRemoteChicken(apiId: String) {
        viewModelScope.launch {
            val success = chickenRepository.deleteRemoteChicken(apiId)
            if (success) {
                loadRemoteChickens()
            }
        }
    }

    fun createRemoteChicken(chicken: ChickenApi) {
        viewModelScope.launch {
            val created = chickenRepository.createRemoteChicken(chicken)
            if (created != null) {
                loadRemoteChickens()
            }
        }
    }

    fun updateRemoteChicken(id: String, chicken: ChickenApi) {
        viewModelScope.launch {
            val updated = chickenRepository.updateRemoteChicken(id, chicken)
            if (updated != null) {
                loadRemoteChickens()
            }
        }
    }

    // ============================================
    // 🟣 SINCRONIZACIÓN API → ROOM
    // ============================================

    fun syncFromApi() {
        viewModelScope.launch {
            chickenRepository.syncFromApi()
        }
    }
}
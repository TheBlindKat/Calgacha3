package com.example.calgacha.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.data.remote.model.ChickenApi
import com.example.calgacha.data.repository.ChickenRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class AddViewModel(private val repository: ChickenRepository) : ViewModel() {

    var name = mutableStateOf("")
    var nameError = mutableStateOf<String?>(null)
    var age = mutableStateOf("")
    var ageError = mutableStateOf<String?>(null)
    var breed = mutableStateOf("")
    var breedError = mutableStateOf<String?>(null)
    var description = mutableStateOf("")
    var descriptionError = mutableStateOf<String?>(null)
    var imageUri = mutableStateOf<Uri?>(null)

    fun onNameChange(value: String) {
        name.value = value
        nameError.value = when {
            value.isBlank() -> "El nombre no puede estar vacío"
            value.length < 3 -> "Debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun onAgeChange(value: String) {
        age.value = value
        ageError.value = when {
            value.isBlank() -> "La edad no puede estar vacía"
            value.toIntOrNull() == null -> "La edad debe ser un número válido"
            value.toIntOrNull() == 0 -> "La edad debe ser mayor a 0"
            else -> null
        }
    }

    fun onBreedChange(value: String) {
        breed.value = value
        breedError.value = when {
            value.isBlank() -> "La raza no puede estar vacía"
            else -> null
        }
    }

    fun onDescriptionChange(value: String) {
        description.value = value
        descriptionError.value = when {
            value.isBlank() -> "La descripción no puede estar vacía"
            else -> null
        }
    }

    fun onImageChange(uri: Uri?) {
        imageUri.value = uri
    }


    fun addChicken(imagePath: String?, onChickenAdded: () -> Unit) {
        viewModelScope.launch {

            // 1️⃣ Guardar en ROOM
            val newChicken = Chicken(
                id = 0,
                nombre = name.value,
                edad = age.value.toIntOrNull() ?: 0,
                raza = breed.value,
                descripcion = description.value,
                imagenUri = imagePath
            )
            repository.insertChicken(newChicken)

            // 2️⃣ Guardar en API remota
            val newChickenApi = ChickenApi(
                nombre = name.value,
                edad = age.value.toIntOrNull() ?: 0,
                raza = breed.value,
                descripcion = description.value,
                imagenUri = imagePath
            )
            repository.createRemoteChicken(newChickenApi)

            onChickenAdded()
        }
    }

    fun isFormValid(): Boolean {
        return nameError.value == null &&
                ageError.value == null &&
                breedError.value == null &&
                descriptionError.value == null &&
                name.value.isNotBlank() &&
                age.value.isNotBlank() &&
                breed.value.isNotBlank() &&
                description.value.isNotBlank()
    }


    fun saveImageToInternalStorage(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "chicken_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }


    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String {
        val fileName = "chicken_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()

        return file.absolutePath
    }
}

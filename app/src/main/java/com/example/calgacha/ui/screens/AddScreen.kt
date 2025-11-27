package com.example.calgacha.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.calgacha.navigation.Routes
import com.example.calgacha.ui.viewmodel.AddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(vm: AddViewModel, navController: NavController) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        bitmap = bmp
        imageUri = null
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        bitmap = null
        vm.onImageChange(uri)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nueva gallina") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = vm.name.value,
                onValueChange = { vm.onNameChange(it) },
                label = { Text("Nombre") },
                isError = vm.nameError.value != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.nameError.value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = vm.age.value,
                onValueChange = { vm.onAgeChange(it) },
                label = { Text("Edad (solo números)") },
                isError = vm.ageError.value != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            vm.ageError.value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = vm.breed.value,
                onValueChange = { vm.onBreedChange(it) },
                label = { Text("Raza") },
                isError = vm.breedError.value != null,
                modifier = Modifier.fillMaxWidth()
            )
            vm.breedError.value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = vm.description.value,
                onValueChange = { vm.onDescriptionChange(it) },
                label = { Text("Descripción") },
                isError = vm.descriptionError.value != null,
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            vm.descriptionError.value?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) -> {
                                takePictureLauncher.launch(null)
                            }
                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("📷 Tomar Foto")
                }

                Button(
                    onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE,

                            ) -> {
                                takePictureLauncher.launch(null)
                            }
                            else -> {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("🖼️ Galería")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            when {
                bitmap != null -> {
                    Text("📸 Foto tomada:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Foto de la gallina",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                imageUri != null -> {
                    Text("🖼️ Imagen seleccionada:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Imagen de la gallina",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Text(
                        "Ninguna imagen seleccionada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Button(
                onClick = {

                    val savedPath = when {
                        bitmap != null -> vm.saveBitmapToInternalStorage(context, bitmap!!)
                        imageUri != null -> vm.saveImageToInternalStorage(context, imageUri!!)
                        else -> null
                    }

                    vm.addChicken(savedPath) {
                        navController.navigate(Routes.HISTORY) {
                            popUpTo(Routes.ADD) { inclusive = true }
                        }
                    }

                },
                enabled = vm.isFormValid(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Gallina")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
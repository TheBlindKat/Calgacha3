package com.example.calgacha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(itemId: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    var item by remember { mutableStateOf<Chicken?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = itemId) {
        item = viewModel.getChickenById(id = itemId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Gallina") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    // ⬇️ BOTÓN EDITAR
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val chicken = item
            if (chicken != null) {

                // Imagen grande
                if (chicken.imagenUri != null) {
                    AsyncImage(
                        model = chicken.imagenUri,
                        contentDescription = "Imagen de ${chicken.nombre}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Información General",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Nombre:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = chicken.nombre,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Edad:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${chicken.edad} años",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Raza:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = chicken.raza,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Descripción:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = chicken.descripcion,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        "Gallina no encontrada",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // ⬇️ DIÁLOGO DE EDICIÓN
        if (showEditDialog && item != null) {
            EditChickenDialog(
                chicken = item!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedChicken ->
                    viewModel.updateChicken(updatedChicken)
                    item = updatedChicken // Actualizar la vista inmediatamente
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun EditChickenDialog(
    chicken: Chicken,
    onDismiss: () -> Unit,
    onSave: (Chicken) -> Unit
) {
    var nombre by remember { mutableStateOf(chicken.nombre) }
    var edad by remember { mutableStateOf(chicken.edad.toString()) }
    var raza by remember { mutableStateOf(chicken.raza) }
    var descripcion by remember { mutableStateOf(chicken.descripcion) }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var edadError by remember { mutableStateOf<String?>(null) }
    var razaError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Gallina") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        nombreError = when {
                            it.isBlank() -> "El nombre no puede estar vacío"
                            it.length < 3 -> "Debe tener al menos 3 caracteres"
                            else -> null
                        }
                    },
                    label = { Text("Nombre") },
                    isError = nombreError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (nombreError != null) {
                    Text(
                        text = nombreError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = edad,
                    onValueChange = {
                        edad = it
                        edadError = when {
                            it.isBlank() -> "La edad no puede estar vacía"
                            it.toIntOrNull() == null -> "Solo números"
                            it.toIntOrNull()!! <= 0 -> "Debe ser mayor a 0"
                            else -> null
                        }
                    },
                    label = { Text("Edad (años)") },
                    isError = edadError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (edadError != null) {
                    Text(
                        text = edadError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = raza,
                    onValueChange = {
                        raza = it
                        razaError = if (it.isBlank()) "La raza no puede estar vacía" else null
                    },
                    label = { Text("Raza") },
                    isError = razaError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (razaError != null) {
                    Text(
                        text = razaError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        descripcionError = if (it.isBlank()) "La descripción no puede estar vacía" else null
                    },
                    label = { Text("Descripción") },
                    isError = descripcionError != null,
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                if (descripcionError != null) {
                    Text(
                        text = descripcionError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val edadInt = edad.toIntOrNull()
                    if (nombreError == null && edadError == null &&
                        razaError == null && descripcionError == null &&
                        nombre.isNotBlank() && edadInt != null &&
                        raza.isNotBlank() && descripcion.isNotBlank()
                    ) {
                        val updatedChicken = chicken.copy(
                            nombre = nombre,
                            edad = edadInt,
                            raza = raza,
                            descripcion = descripcion
                        )
                        onSave(updatedChicken)
                    }
                },
                enabled = nombreError == null && edadError == null &&
                        razaError == null && descripcionError == null
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
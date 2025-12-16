package com.example.calgacha.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    navController: NavController,
    onItemClick: (Int) -> Unit
) {
    val chickens = viewModel.chickens.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial Local") },
                actions = {
                    IconButton(onClick = { viewModel.syncFromApi() }) {
                        Icon(Icons.Default.Refresh, "Sincronizar desde API")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(chickens.value) { chicken ->
                HistoryItemRow(
                    chicken = chicken,
                    onClick = { onItemClick(chicken.id) },
                    onDelete = { viewModel.deleteChicken(chicken) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun HistoryItemRow(chicken: Chicken, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chicken.imagenUri != null) {
                AsyncImage(
                    model = chicken.imagenUri,
                    contentDescription = "Imagen de ${chicken.nombre}",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Sin imagen",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chicken.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Edad: ${chicken.edad} años",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Raza: ${chicken.raza}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = chicken.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
package com.example.calgacha.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.calgacha.data.local.model.Chicken
import com.example.calgacha.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: MainViewModel, navController: NavController, onItemClick: (Int) -> Unit) {
    val localChickens = viewModel.chickens.collectAsState()
    val apiChickens = viewModel.apiChickens.observeAsState(emptyList())



    Scaffold(
        topBar = { TopAppBar(title = { Text("Historial") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            val all = apiChickens.value + localChickens.value

            items(all) { chicken ->
                HistoryItemRow(
                    chicken = chicken as Chicken,
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
            // ⬇️ Miniatura de imagen
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
                // Ícono por defecto si no hay imagen
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
                    text = "Edad: ${chicken.edad} meses",
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


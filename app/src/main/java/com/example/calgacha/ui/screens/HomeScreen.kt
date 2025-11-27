package com.example.calgacha.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calgacha.R

import com.example.calgacha.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavController) {

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Lista de gallinas", style = MaterialTheme.typography.titleLarge) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.gallina_fachera),
                contentDescription = "Welcome Image"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "¡Bienvenido a Calgacha Industries!.",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Agrega tu primera gallina insana",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

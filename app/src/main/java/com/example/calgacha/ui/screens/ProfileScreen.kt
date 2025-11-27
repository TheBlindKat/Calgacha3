package com.example.calgacha.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.calgacha.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: LoginViewModel) {
    val userEmail = viewModel.userEmail.collectAsState().value

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (userEmail == null) {
                // Show login form
                TextField(
                    value = viewModel.email.value,
                    onValueChange = { viewModel.email.value = it },
                    label = { Text("Email") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.password.value = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation()
                )

                viewModel.errorMessage.value?.let {
                    Text(it, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.validate() }) {
                    Text("Ingresar")
                }
            } else {
                // Show welcome message
                Text("Bienvenido, $userEmail")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.logout() }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

package com.example.calgacha.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calgacha.ui.viewmodel.LoginViewModel

@Composable
fun LoginForm(viewModel: LoginViewModel = viewModel()) {
    Column(Modifier.padding(16.dp)) {
        TextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Email") }
        )

        TextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (viewModel.errorMessage.value != null) {
            Text(viewModel.errorMessage.value!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.validate() }) {
            Text("Ingresar")
        }
    }
}

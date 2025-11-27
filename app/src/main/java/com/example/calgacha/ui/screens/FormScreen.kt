package com.example.calgacha.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calgacha.ui.viewmodel.FormViewModel

@Composable
fun FormWithStateFlow(viewModel: FormViewModel = viewModel()) {
    val name by viewModel.name.collectAsState()
    val isValid by viewModel.isValid.collectAsState()

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { println("Nombre enviado: $name") },
            enabled = isValid
        ) {
            Text("Guardar")
        }
    }
}

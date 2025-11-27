package com.example.calgacha.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ValidatedForm(){
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    Column (Modifier.padding(16.dp)){
        TextField(
            value=email,
            onValueChange = {
                email = it
                emailError=if(!it.contains("@"))"Email invalido" else null
            },
            label = { Text("Email") },
            isError = emailError != null
        )
        if (emailError != null){
            Text(emailError!!, color = Color.Red, fontSize = 12.sp)

        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (emailError == null && email.isNotBlank()){
                println("Email valido: $email")
            }
        }) {
            Text("Registrar")
        }
    }
}

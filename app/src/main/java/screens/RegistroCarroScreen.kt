package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.auth.FirebaseAuthManager
import com.example.leyesmx.data.FirebaseFirestoreManager

@Composable
fun RegistroCarroScreen(
    onRegistroExitoso: () -> Unit
) {
    val authManager = remember { FirebaseAuthManager() }
    val firestoreManager = remember { FirebaseFirestoreManager() }

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registrar carro", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = placas, onValueChange = { placas = it }, label = { Text("Placas") }, modifier = Modifier.fillMaxWidth())

        mensajeError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val userId = authManager.obtenerUsuarioId()
            if (userId != null && marca.isNotBlank() && modelo.isNotBlank() && placas.isNotBlank()) {
                firestoreManager.guardarCarro(userId, marca, modelo, placas)
                onRegistroExitoso()
            } else {
                mensajeError = "Completa todos los campos o inicia sesión"
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar carro")
        }
    }
}
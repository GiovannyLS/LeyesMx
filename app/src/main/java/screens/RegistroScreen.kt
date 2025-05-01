package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.auth.FirebaseAuthManager
import com.example.leyesmx.data.FirebaseFirestoreManager

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit
) {
    val authManager = remember { FirebaseAuthManager() }
    val firestoreManager = remember { FirebaseFirestoreManager() }

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())

        mensajeError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank() || edad.isBlank() || ciudad.isBlank()) {
                mensajeError = "Completa todos los campos"
                return@Button
            }

            authManager.registrarUsuario(
                correo = correo,
                contraseña = contrasena,
                onSuccess = {
                    val userId = authManager.obtenerUsuarioId()
                    if (userId != null) {
                        firestoreManager.guardarPerfil(userId, nombre, edad.toIntOrNull() ?: 0, ciudad)
                        onRegistroExitoso()
                    } else {
                        mensajeError = "Error interno al obtener ID"
                    }
                },
                onError = { error ->
                    mensajeError = error
                }
            )
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Registrarse")
        }
    }
}
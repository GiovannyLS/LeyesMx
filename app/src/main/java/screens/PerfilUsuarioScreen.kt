package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.auth.FirebaseAuthManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun PerfilUsuarioScreen() {
    val auth = FirebaseAuthManager()
    val userId = auth.obtenerUsuarioId()
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            Firebase.firestore.collection("usuarios")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    nombre = document.getString("nombre") ?: ""
                    email = document.getString("email") ?: ""
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre: $nombre")
        Text("Correo: $email")
    }
}
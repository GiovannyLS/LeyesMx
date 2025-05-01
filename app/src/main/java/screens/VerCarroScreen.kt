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
fun VerCarroScreen() {
    val auth = FirebaseAuthManager()
    val userId = auth.obtenerUsuarioId()
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            Firebase.firestore.collection("usuarios")
                .document(userId)
                .collection("carros")
                .limit(1) // Solo uno por ahora
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        val doc = docs.documents[0]
                        marca = doc.getString("marca") ?: ""
                        modelo = doc.getString("modelo") ?: ""
                        placas = doc.getString("placas") ?: ""
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Mi carro", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Marca: $marca")
        Text("Modelo: $modelo")
        Text("Placas: $placas")
    }
}
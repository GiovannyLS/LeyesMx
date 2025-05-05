package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.leyesmx.auth.FirebaseAuthManager
import com.example.leyesmx.data.FirebaseFirestoreManager
import com.example.leyesmx.viewmodel.userViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegistroCarroScreen(userViewModel: userViewModel, navController: NavHostController) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var placas by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registrar Vehículo", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = marca,
            onValueChange = { marca = it },
            label = { Text("Marca") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = modelo,
            onValueChange = { modelo = it },
            label = { Text("Modelo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = placas,
            onValueChange = { placas = it },
            label = { Text("Placas") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (marca.isNotBlank() && modelo.isNotBlank() && placas.isNotBlank()) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        val carroData = hashMapOf(
                            "marca" to marca,
                            "modelo" to modelo,
                            "placas" to placas
                        )
                        Firebase.firestore.collection("carros").document(uid).set(carroData)
                            .addOnSuccessListener {
                                println("Vehículo registrado correctamente")
                                userViewModel.registrarCarro(marca, modelo, placas)
                                navController.navigate("ver_carro") {
                                    popUpTo("registro_carro") { inclusive = true }
                                }
                            }
                            .addOnFailureListener {
                                println("Error al registrar vehículo: ${it.message}")
                                errorMsg = "Error al guardar el vehículo. Intenta de nuevo."
                            }
                    } else {
                        errorMsg = "Usuario no autenticado."
                    }
                } else {
                    errorMsg = "Por favor, completa todos los campos."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        errorMsg?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
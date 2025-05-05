package com.example.leyesmx.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.leyesmx.viewmodel.userViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.leyesmx.model.Carro

@Composable
fun LoginScreen(navController: NavController, userViewModel: userViewModel) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (correo.isNotBlank() && contrasena.isNotBlank()) {
                    cargando = true
                    error = null
                    auth.signInWithEmailAndPassword(correo.trim(), contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                if (uid != null) {
                                    // Obtener datos del usuario
                                    db.collection("usuarios").document(uid).get()
                                        .addOnSuccessListener { userDoc ->
                                            val nombre = userDoc.getString("nombre") ?: "Usuario"
                                            val email = userDoc.getString("correo") ?: correo

                                            // Obtener datos del carro
                                            db.collection("carros").document(uid).get()
                                                .addOnSuccessListener { carroDoc ->
                                                    val carro = if (carroDoc.exists()) {
                                                        Carro(
                                                            marca = carroDoc.getString("marca") ?: "",
                                                            modelo = carroDoc.getString("modelo") ?: "",
                                                            placas = carroDoc.getString("placas") ?: ""
                                                        )
                                                    } else null

                                                    // Guardar todo en el viewModel
                                                    userViewModel.login(uid, nombre, email, carro)
                                                    cargando = false
                                                    navController.navigate("menu") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                                .addOnFailureListener {
                                                    // Si falla carro, aún así continúa
                                                    userViewModel.setUsuario(uid, nombre, email, null)
                                                    cargando = false
                                                    navController.navigate("menu") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                        }
                                        .addOnFailureListener {
                                            error = "No se pudo obtener la información del usuario"
                                            cargando = false
                                        }
                                } else {
                                    error = "Error al obtener UID del usuario"
                                    cargando = false
                                }
                            } else {
                                error = "Correo o contraseña incorrectos"
                                cargando = false
                            }
                        }
                } else {
                    error = "Completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ingresando...")
            } else {
                Text("Ingresar")
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("¿No tienes cuenta? ")
            Text(
                text = "Regístrate aquí",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("registro_usuario")
                }
            )
        }
    }
}
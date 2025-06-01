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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock

@Composable
fun LoginScreen(navController: NavController, userViewModel: userViewModel) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo o encabezado visual
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Icono de seguridad",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Bienvenido a LeyesMX",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
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
                                        db.collection("usuarios").document(uid).get()
                                            .addOnSuccessListener { userDoc ->
                                                val nombre = userDoc.getString("nombre") ?: "Usuario"
                                                val email = userDoc.getString("correo") ?: correo

                                                db.collection("carros").document(uid).get()
                                                    .addOnSuccessListener { carroDoc ->
                                                        val carro = if (carroDoc.exists()) {
                                                            Carro(
                                                                marca = carroDoc.getString("marca") ?: "",
                                                                modelo = carroDoc.getString("modelo") ?: "",
                                                                placas = carroDoc.getString("placas") ?: ""
                                                            )
                                                        } else null

                                                        userViewModel.login(uid, nombre, email, carro)
                                                        cargando = false
                                                        navController.navigate("menu") {
                                                            popUpTo("login") { inclusive = true }
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        userViewModel.setUsuario(uid, nombre, email, null)
                                                        cargando = false
                                                        navController.navigate("menu") {
                                                            popUpTo("login") { inclusive = true }
                                                        }
                                                    }
                                            }
                                            .addOnFailureListener {
                                                error = "No se pudo obtener los datos del usuario"
                                                cargando = false
                                            }
                                    } else {
                                        error = "Error al obtener ID de usuario"
                                        cargando = false
                                    }
                                } else {
                                    error = "Correo o contraseña incorrectos"
                                    cargando = false
                                }
                            }
                    } else {
                        error = "Por favor completa todos los campos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ingresando...")
                } else {
                    Text("Ingresar", style = MaterialTheme.typography.labelLarge)
                }
            }

            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("¿No tienes cuenta?", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        navController.navigate("registro_usuario")
                    }
                )
            }
        }
    }
}


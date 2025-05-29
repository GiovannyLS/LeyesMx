package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

@Composable
fun RegistroUsuarioScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var correoError by remember { mutableStateOf(false) }
    var contrasenaError by remember { mutableStateOf(false) }
    val esCorreoValido = android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val esContrasenaSegura = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$").matcher(contrasena).matches()

    val usuario = hashMapOf(
        "nombre" to nombre,
        "correo" to correo
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                correoError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Correo electrónico") },
            isError = correoError,
            modifier = Modifier.fillMaxWidth()
        )
        if (correoError) {
            Text("Correo inválido", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                contrasenaError = !Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")
                    .matcher(it).matches()
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = contrasenaError,
            modifier = Modifier.fillMaxWidth()
        )
        if (contrasenaError) {
            Text(
                "Debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo.",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                    mensaje = "Completa todos los campos"
                    return@Button
                }

                if (!esCorreoValido) {
                    mensaje = "Correo electrónico inválido"
                    return@Button
                }

                if (!esContrasenaSegura) {
                    mensaje = "Contraseña insegura"
                    return@Button
                }

                    cargando = true
                    mensaje = ""



                    auth.createUserWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                if (userId != null) {
                                    val usuario = hashMapOf(
                                        "nombre" to nombre,
                                        "correo" to correo,
                                        "contrasena" to contrasena
                                    )
                                    db.collection("usuarios").document(userId)
                                        .set(usuario)
                                        .addOnSuccessListener {
                                            mensaje = "Registro exitoso"
                                            cargando = false
                                            navController.navigate("login")
                                        }
                                        .addOnFailureListener { e ->
                                            mensaje = "Error al guardar en Firestore: ${e.message}"
                                            cargando = false
                                        }
                                } else {
                                    mensaje = "Error: usuario no disponible después del registro"
                                    cargando = false
                                }
                            } else {
                                mensaje = "Error: ${task.exception?.message}"
                                cargando = false
                            }
                        }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrando...")
            } else {
                Text("Registrarse")
            }
        }

        if (mensaje.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}
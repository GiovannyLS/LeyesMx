package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.auth.FirebaseAuthManager
import com.example.leyesmx.viewmodel.userViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavHostController

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.material.icons.filled.Check
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.ui.text.input.TextFieldValue









@Composable
fun VerCarroScreen(userViewModel: userViewModel, navController: NavHostController) {
    val usuario = userViewModel.usuario
    var editando by remember { mutableStateOf(false) }

    var marca by remember { mutableStateOf(usuario?.carro?.marca ?: "") }
    var modelo by remember { mutableStateOf(usuario?.carro?.modelo ?: "") }
    var placas by remember { mutableStateOf(usuario?.carro?.placas ?: "") }

    val db = Firebase.firestore
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (usuario?.carro != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (editando) {
                            if (marca.isBlank() || modelo.isBlank() || placas.isBlank()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("⚠️ Todos los campos son obligatorios")
                                }
                            } else if (uid != null) {
                                val nuevoCarro = hashMapOf(
                                    "marca" to marca,
                                    "modelo" to modelo,
                                    "placas" to placas
                                )

                                db.collection("usuarios").document(uid)
                                    .update("carro", nuevoCarro)
                                    .addOnSuccessListener {
                                        userViewModel.actualizarCarro(marca, modelo, placas)
                                        editando = false
                                        focusManager.clearFocus()
                                        scope.launch {
                                            snackbarHostState.showSnackbar("✅ Cambios guardados")
                                        }
                                    }
                                    .addOnFailureListener {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("❌ Error: ${it.message}")
                                        }
                                    }
                            }
                        } else {
                            editando = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (editando) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(if (editando) "Guardar" else "Editar")
                    },
                    containerColor = if (editando) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Información del Vehículo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            AnimatedVisibility(visible = usuario?.carro != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = if (editando) "Editar Datos" else "Vehículo Registrado",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Divider()

                        if (editando) {
                            VehiculoInputField("Marca", marca, marca.isBlank()) { marca = it }
                            VehiculoInputField("Modelo", modelo, modelo.isBlank()) { modelo = it }
                            VehiculoInputField("Placas", placas, placas.isBlank()) { placas = it }
                        } else {
                            VehiculoInfoItem("Marca", marca)
                            VehiculoInfoItem("Modelo", modelo)
                            VehiculoInfoItem("Placas", placas)
                        }
                    }
                }
            }

            if (usuario?.carro == null) {
                EmptyCarSection(navController)
            }
        }
    }
}


@Composable
fun VehiculoInputField(label: String, value: String, showError: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = showError,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    if (showError) {
        Text(
            text = "Este campo es obligatorio",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun VehiculoInfoItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun EmptyCarSection(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aún no has registrado un vehículo", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("registro_carro") }) {
            Text("Registrar Vehículo")
        }
    }
}









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
import androidx.compose.ui.Alignment


@Composable
fun VerCarroScreen(userViewModel: userViewModel, navController: NavHostController) {
    val carro = userViewModel.carro
    val usuario = userViewModel.usuario


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mi Vehículo", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = { navController.navigate("registro_carro") },
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar información del carro"
            )
        }


        if (usuario != null && usuario.carro != null) {
            val carro = usuario.carro!!
            Column {
                Text("🚗 Carro registrado:")
                Carro3D(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Text("• Marca: ${carro.marca}")
                Text("• Modelo: ${carro.modelo}")
                Text("• Placas: ${carro.placas}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("registro_carro")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar Información del Vehículo")
                }
            }
        } else {
            Column {
                Text("🚫 Aún no has registrado un carro.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    navController.navigate("registro_carro")
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Text("Registrar Carro")
                }
            }
        }




        if (carro != null){
            carro?.let {
                Text("Marca: ${it.marca}")
                Text("Modelo: ${it.modelo}")
                Text("Placas: ${it.placas}")
            } ?: run {
                Text("No hay información del vehículo registrada.")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
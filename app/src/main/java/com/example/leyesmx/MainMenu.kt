package com.example.leyesmx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MainMenu(navController: NavHostController) {
    val secciones = listOf(
        "constitucion" to "Constitución",
        "transito" to "Tránsito",
        "multas" to "Multas",
        "verificacion" to "Verificación",
        "tenencia" to "Tenencia",
        "noticias" to "Noticias"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        secciones.forEach { (ruta, titulo) ->
            Button(
                onClick = { navController.navigate(ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(titulo)
            }
        }
    }
}
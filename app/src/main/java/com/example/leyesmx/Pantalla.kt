package com.example.leyesmx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Pantalla(titulo: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )
        Divider()
        Text(
            text = "Aquí irá el contenido completo sobre \"$titulo\". Puedes mostrar artículos, listas, o enlaces relevantes.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

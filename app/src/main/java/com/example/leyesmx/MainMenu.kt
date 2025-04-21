package com.example.leyesmx

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainMenu(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuCard("Constitución Mexicana", Icons.Default.Description) {
            navController.navigate("constitucion")
        }
        MenuCard("Reglamento de Tránsito", Icons.Default.DirectionsCar) {
            navController.navigate("transito")
        }
        MenuCard("Multas", Icons.Default.Money) {
            navController.navigate("multas")
        }
        MenuCard("Verificación Vehicular", Icons.Default.CheckCircle) {
            navController.navigate("verificacion")
        }
        MenuCard("Pago de Tenencia", Icons.Default.Receipt) {
            navController.navigate("tenencia")
        }
        MenuCard("Noticias Legales", Icons.Default.Article) {
            navController.navigate("noticias")
        }
    }
}

@Composable
fun MenuCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
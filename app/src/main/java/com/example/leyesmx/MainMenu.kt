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

import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bienvenido a LeyesMX",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        MenuCard("Constitución Mexicana", Icons.Default.Description, Color(0xFF4CAF50)) {
            navController.navigate("constitucion")
        }
        MenuCard("Reglamento de Tránsito", Icons.Default.DirectionsCar, Color(0xFF2196F3)) {
            navController.navigate("transito")
        }
        MenuCard("Multas", Icons.Default.AttachMoney, Color(0xFFF44336)) {
            navController.navigate("multas")
        }
        MenuCard("Verificación Vehicular", Icons.Default.Build, Color(0xFFFF9800)) {
            navController.navigate("verificacion")
        }
        MenuCard("Pago de Tenencia", Icons.Default.ReceiptLong, Color(0xFF9C27B0)) {
            navController.navigate("tenencia")
        }
        MenuCard("Noticias Legales", Icons.Default.Article, Color(0xFF3F51B5)) {
            navController.navigate("noticias")
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}
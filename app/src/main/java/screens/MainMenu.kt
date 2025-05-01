package com.example.leyesmx.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.leyesmx.R

@Composable
fun MainMenu(navController: NavHostController) {
    val items = listOf(
        Triple("Constitución", R.drawable.ic_tenencia, "constitucion"),
        Triple("Tránsito", R.drawable.ic_tenencia, "transito"),
        Triple("Tenencia", R.drawable.ic_tenencia, "tenencia"),
        Triple("Verificación", R.drawable.ic_verificacion, "verificacion"),
        Triple("Multas", R.drawable.ic_multas, "multas"),
        Triple("Noticias", R.drawable.ic_noticias, "noticias")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { (title, icon, route) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { navController.navigate(route) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = title,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(title, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f)) // para mantener equilibrio si es impar
                }
            }
        }
    }
}
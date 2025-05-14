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
import com.example.leyesmx.viewmodel.userViewModel

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed


@Composable
fun MainMenu(navController: NavHostController, userViewModel: userViewModel) {
    val usuario = userViewModel.usuario

    val items = listOf(
        Triple("Constitución", R.drawable.ic_constitucion, "constitucion"),
        Triple("Tránsito", R.drawable.ic_transito, "transito"),
        Triple("Tenencia", R.drawable.ic_tenencia, "tenencia"),
        Triple("Verificación", R.drawable.ic_verificacion, "verificacion"),
        Triple("Noticias", R.drawable.ic_noticias, "noticias")
    )

    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.primary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¡Bienvenido, ${usuario?.nombre ?: "Invitado"}!",
            style = MaterialTheme.typography.headlineSmall
        )


        Spacer(modifier = Modifier.height(24.dp))

        items.forEachIndexed { index, (title, icon, route) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable { navController.navigate(route) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors[index % colors.size]),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
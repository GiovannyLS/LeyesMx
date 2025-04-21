// ruta: app/src/main/java/com/example/leyesmx/screens/NoticiasScreen.kt

package com.example.leyesmx.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.leyesmx.R
import com.example.leyesmx.data.NoticiasProvider

@Composable
fun NoticiasScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(NoticiasProvider.noticias) { noticia ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_noticias),
                        contentDescription = "Ícono de noticias",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 16.dp)
                    )
                    Column {
                        Text(text = noticia.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(text = noticia.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Fuente: ${noticia.fuente} | ${noticia.fecha}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
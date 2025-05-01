package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.viewmodel.TransitoViewModel

@Composable
fun TransitoScreen(viewModel: TransitoViewModel = TransitoViewModel()){

    val articulos by viewModel.articulos.collectAsState()

    LazyColumn (modifier = Modifier.fillMaxSize().padding(16.dp)){

        items(articulos){ articulo ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
              Column (modifier = Modifier.padding(16.dp)) {
                  Text(articulo.titulo, style = MaterialTheme.typography.titleMedium)
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(articulo.contenido, style = MaterialTheme.typography.titleMedium)
              }
            }
        }
    }
}
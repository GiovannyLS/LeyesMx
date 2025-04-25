package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.viewmodel.ConstitucionViewModel

@ExperimentalMaterial3Api
@Composable
fun ConstitucionScreen(viewModel: ConstitucionViewModel) {
    val articulos = viewModel.articulos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Constitución Mexicana") }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)
        ) {
            items(articulos.value) { articulo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = articulo.titulo, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = articulo.contenido, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
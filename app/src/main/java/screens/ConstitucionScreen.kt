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
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.example.leyesmx.model.Articulo  // Asegúrate de tener esta clase de datos
import androidx.compose.foundation.shape.RoundedCornerShape

@ExperimentalMaterial3Api
@Composable
fun ConstitucionScreen(viewModel: ConstitucionViewModel) {
    val articulos = viewModel.articulos.collectAsState().value
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val groupedArticulos = articulos
        .filter {
            it.titulo.contains(searchText.text, ignoreCase = true) ||
                    it.contenido.contains(searchText.text, ignoreCase = true)
        }
        .groupBy { it.titulo.substringBefore(":").trim() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Constitución Mexicana") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                placeholder = { Text("Buscar artículo...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedArticulos.forEach { (seccion, articulosEnSeccion) ->
                    item {
                        ExpandableSection(title = seccion, articulos = articulosEnSeccion)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, articulos: List<Articulo>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    articulos.forEach { articulo ->
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            Text(
                                text = articulo.titulo,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = articulo.contenido,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
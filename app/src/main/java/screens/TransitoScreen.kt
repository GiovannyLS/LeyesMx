package com.example.leyesmx.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.viewmodel.TransitoViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.text.buildAnnotatedString

import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import com.example.leyesmx.data.glosario





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitoScreen(viewModel: TransitoViewModel = TransitoViewModel()) {
    val articulos by viewModel.articulos.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filteredArticulos = articulos.filter {
        it.titulo.contains(searchQuery.text, ignoreCase = true) ||
                it.contenido.contains(searchQuery.text, ignoreCase = true)
    }

    var showDropdown by remember { mutableStateOf(false) }
    var listState = rememberLazyListState()
    var glossaryWord by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("Reglamento de Tránsito")
                        IconButton(onClick = { showDropdown = !showDropdown }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Índice")
                        }
                    }
                },
                actions = {
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        filteredArticulos.take(10).forEachIndexed { index, articulo ->
                            DropdownMenuItem(
                                text = { Text(articulo.titulo.take(30)) },
                                onClick = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                    }
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {

            // Simulación de notificación importante
            if (articulos.isNotEmpty()) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar artículo") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                itemsIndexed(filteredArticulos) { _, articulo ->
                    var expanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { expanded = !expanded },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = articulo.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            AnimatedVisibility(
                                visible = expanded,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val palabrasClave = listOf("infracción",  "peatón", "licencia")

                                    val annotatedText = buildAnnotatedString {
                                        val palabras = articulo.contenido.split(" ")
                                        palabras.forEachIndexed { index, palabra ->
                                            val cleanWord = palabra.trim().lowercase().removeSuffix(".").removeSuffix(",")
                                            if (glosario.containsKey(cleanWord)) {
                                                pushStringAnnotation(tag = "glosario", annotation = cleanWord)
                                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                                    append("$palabra ")
                                                }
                                                pop()
                                            } else {
                                                append("$palabra ")
                                            }
                                        }
                                    }

                                    ClickableText(
                                        text = annotatedText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        onClick = { offset ->
                                            annotatedText.getStringAnnotations(tag = "glosario", start = offset, end = offset)
                                                .firstOrNull()?.let { annotation ->
                                                    glossaryWord = annotation.item
                                                }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    glossaryWord?.let { word ->
        AlertDialog(
            onDismissRequest = { glossaryWord = null },
            title = { Text("Definición: $word") },
            text = {
                Text(
                    when (word.lowercase()) {
                        "infracción" -> "Violación de una norma de tránsito que puede implicar una sanción."
                        "vehículo" -> "Medio de transporte terrestre como automóviles, motocicletas o camiones."
                        "peatón" -> "Persona que transita a pie por la vía pública."
                        "licencia" -> "Una licencia de conducir es un documento oficial, personal e intransferible, emitido por una autoridad competente (como el Ministerio de Transporte o la Secretaría de Movilidad) que autoriza a una persona a conducir un vehículo en vía pública"
                        else -> "Sin definición disponible."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { glossaryWord = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}
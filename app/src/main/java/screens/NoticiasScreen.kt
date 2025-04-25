package com.example.leyesmx.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.leyesmx.R
import com.example.leyesmx.viewmodel.NoticiasViewModel
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticiasScreen(viewModel: NoticiasViewModel) {
    val noticias by viewModel.noticias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Buscador
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Buscar noticias...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading),
            onRefresh = { viewModel.refrescar() }
        ) {
            when {
                isLoading && noticias.isEmpty() -> {
                    // Spinner mientras se cargan noticias por primera vez
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                noticias.isEmpty() -> {
                    // No hay noticias
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron noticias.",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(noticias) { noticia ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable { uriHandler.openUri(noticia.url) },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            ImageRequest.Builder(LocalContext.current)
                                                .data(noticia.urlToImage)
                                                .crossfade(true)
                                                .build()
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        text = noticia.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }

                        item {
                            if (!isLoading && searchQuery.isEmpty()) {
                                Button(
                                    onClick = { viewModel.cargarNoticias() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text("Cargar más noticias")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
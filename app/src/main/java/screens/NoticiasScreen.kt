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
//import com.example.leyesmx.data.NoticiasProvider

import com.example.leyesmx.model.Article
import com.example.leyesmx.data.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.*
import com.example.leyesmx.viewmodel.NoticiasViewModel
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticiasScreen(viewModel: NoticiasViewModel) {
    val noticias by viewModel.noticias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isLoading),
        onRefresh = { viewModel.refrescar() }
    ) {
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
                if (!isLoading) {
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
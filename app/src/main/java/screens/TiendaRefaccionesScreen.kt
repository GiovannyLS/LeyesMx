package com.example.leyesmx.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leyesmx.viewmodel.TiendaViewModel

@Composable
fun TiendaRefaccionesScreen(viewModel: TiendaViewModel = viewModel()) {
    val context = LocalContext.current // ✅ Obtén el contexto composable aquí
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.buscarRefaccion(it)
            },
            label = { Text("Buscar refacción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.loading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(viewModel.productos) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = producto.thumbnail,
                                contentDescription = producto.title,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(producto.title)
                                Text("💲${producto.price}")
                                TextButton(onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(producto.permalink))
                                    context.startActivity(intent) // ✅ Aquí ya no da error
                                }) {
                                    Text("Ver producto")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
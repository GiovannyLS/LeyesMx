package com.example.leyesmx.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.leyesmx.viewmodel.TiendaViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

@Composable
fun TiendaScreen(tiendaViewModel: TiendaViewModel = viewModel()) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val productos = remember { mutableStateListOf<Producto>() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("🔧 Buscar Refacciones", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar en Mercado Libre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val query = searchText.text.trim()
            if (query.isNotEmpty()) {
                coroutineScope.launch {
                    val fetchedItems = searchMercadoLibre(query)
                    productos.clear()
                    productos.addAll(fetchedItems)
                }
            }
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(producto.thumbnail),
                            contentDescription = producto.title,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(producto.title, style = MaterialTheme.typography.titleMedium)
                            Text("Precio: \$${producto.price}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

data class Producto(
    val title: String,
    val price: Double,
    val thumbnail: String
)

suspend fun searchMercadoLibre(query: String): List<Producto> {
    val client = HttpClient(CIO)
    return try {
        val response: String = client.get("https://api.mercadolibre.com/sites/MLM/search?q=${query}").bodyAsText()
        val json = Json.parseToJsonElement(response).jsonObject
        val results = json["results"]?.jsonArray ?: JsonArray(emptyList())

        results.map {
            Producto(
                title = it.jsonObject["title"]?.jsonPrimitive?.content ?: "Sin título",
                price = it.jsonObject["price"]?.jsonPrimitive?.double ?: 0.0,
                thumbnail = it.jsonObject["thumbnail"]?.jsonPrimitive?.content ?: ""
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    } finally {
        client.close()
    }
}
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
    import android.content.Context
    import android.content.Intent
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.material.icons.filled.Share
    import androidx.compose.animation.slideInVertically
    import androidx.compose.animation.slideOutVertically
    import androidx.compose.material.icons.filled.MoreVert
    import androidx.compose.runtime.saveable.rememberSaveable
    import com.example.leyesmx.model.ArticuloTransito
    import androidx.compose.material.icons.filled.ExpandLess
    import androidx.compose.material.icons.filled.ExpandMore



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TransitoScreen(viewModel: TransitoViewModel = TransitoViewModel()) {
        val reglamento = viewModel.reglamento.collectAsState().value

        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        val context = LocalContext.current
        var glossaryWord by remember { mutableStateOf<String?>(null) }

        val listState = rememberLazyListState()

        // Estados para expansión
        val expandedTitulos = remember { mutableStateMapOf<String, Boolean>() }
        val expandedCapitulos = remember { mutableStateMapOf<String, Boolean>() }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Reglamento de Tránsito") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                reglamento?.let { reglamentoData ->

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        reglamentoData.titulos.forEach { titulo ->
                            val isTituloExpanded = expandedTitulos[titulo.nombre] ?: false

                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                            colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )

                                {
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                titulo.nombre,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        },
                                        trailingContent = {
                                            IconButton(onClick = {
                                                expandedTitulos[titulo.nombre] = !isTituloExpanded
                                            }) {
                                                Icon(
                                                    imageVector = if (isTituloExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                    contentDescription = if (isTituloExpanded) "Colapsar" else "Expandir"
                                                )
                                            }
                                        },
                                        modifier = Modifier.clickable {
                                            expandedTitulos[titulo.nombre] = !isTituloExpanded
                                        }
                                    )
                                }
                            }

                            if (isTituloExpanded) {
                                titulo.capitulos.forEach { capitulo ->
                                    val isCapituloExpanded = expandedCapitulos[capitulo.nombre] ?: false

                                    item {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 4.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(2.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        )

                                        {
                                            ListItem(
                                                headlineContent = {
                                                    Text(
                                                        capitulo.nombre,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                },
                                                trailingContent = {
                                                    IconButton(onClick = {
                                                        expandedCapitulos[capitulo.nombre] = !isCapituloExpanded
                                                    }) {
                                                        Icon(
                                                            imageVector = if (isCapituloExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                            contentDescription = if (isCapituloExpanded) "Colapsar" else "Expandir"
                                                        )
                                                    }
                                                },
                                                modifier = Modifier.clickable {
                                                    expandedCapitulos[capitulo.nombre] = !isCapituloExpanded
                                                }
                                            )
                                        }
                                    }

                                    if (isCapituloExpanded) {
                                        items(
                                            capitulo.articulos.filter {
                                                it.titulo.contains(searchQuery.text, true) ||
                                                        it.contenido.contains(searchQuery.text, true)
                                            }
                                        ) { articulo ->
                                            var expanded by remember { mutableStateOf(false) }
                                            var showHelp by remember { mutableStateOf(false) }

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                                    .clickable {
                                                        expanded = !expanded
                                                        if (!expanded) showHelp = false
                                                    },
                                                shape = RoundedCornerShape(12.dp),
                                                elevation = CardDefaults.cardElevation(2.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                                )
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = articulo.titulo,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )

                                                    AnimatedVisibility(visible = expanded) {
                                                        Column {
                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            val annotatedText = buildAnnotatedString {
                                                                val palabras = articulo.contenido.split(" ")
                                                                palabras.forEach { palabra ->
                                                                    val cleanWord = palabra.trim().lowercase()
                                                                        .removeSuffix(".")
                                                                        .removeSuffix(",")
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
                                                                    annotatedText.getStringAnnotations("glosario", offset, offset)
                                                                        .firstOrNull()?.let {
                                                                            glossaryWord = it.item
                                                                        }
                                                                }
                                                            )

                                                            if (articulo.fracciones.isNotEmpty()) {
                                                                Spacer(modifier = Modifier.height(8.dp))
                                                                Text("Fracciones:", style = MaterialTheme.typography.labelMedium)
                                                                articulo.fracciones.forEach { fraccion ->
                                                                    Text("• ${fraccion.texto}", style = MaterialTheme.typography.bodySmall)
                                                                    if (fraccion.descripcionInfraccion.isNotBlank()) {
                                                                        Text(
                                                                            text = "⚠ ${fraccion.descripcionInfraccion}",
                                                                            color = Color.Red,
                                                                            style = MaterialTheme.typography.bodySmall,
                                                                            modifier = Modifier.padding(start = 8.dp)
                                                                        )
                                                                    }
                                                                    Spacer(modifier = Modifier.height(4.dp))
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(8.dp))

                                                            Row(
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                modifier = Modifier.fillMaxWidth()
                                                            ) {
                                                                TextButton(onClick = { showHelp = !showHelp }) {
                                                                    Text(if (showHelp) "Ocultar ayuda" else "¿Qué pasa si no cumplo esto?")
                                                                }

                                                                IconButton(onClick = {
                                                                    val textoCompartir = "${articulo.titulo}\n\n${articulo.contenido}"
                                                                    shareText(context, textoCompartir)
                                                                }) {
                                                                    Icon(Icons.Default.Share, contentDescription = "Compartir artículo")
                                                                }
                                                            }

                                                            AnimatedVisibility(visible = showHelp) {
                                                                Text(
                                                                    text = articulo.descripcionInfraccion,
                                                                    color = Color.Red,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    modifier = Modifier.padding(top = 8.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
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
                        text = { Text(glosario[word] ?: "Sin definición disponible.") },
                        confirmButton = {
                            TextButton(onClick = { glossaryWord = null }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    }


    fun shareText(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir con..."))
    }
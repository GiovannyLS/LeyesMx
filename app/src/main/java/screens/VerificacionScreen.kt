    package com.example.leyesmx.screens

    import android.content.pm.PackageManager
    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.unit.dp
    import androidx.core.content.ContextCompat
    import com.example.leyesmx.viewmodel.userViewModel
    import com.google.android.gms.location.LocationServices
    import com.google.android.gms.maps.GoogleMap
    import com.google.android.gms.maps.model.CameraPosition
    import com.google.maps.android.compose.*
    import com.google.android.gms.maps.model.LatLng
    import com.google.accompanist.permissions.*
    import com.google.firebase.firestore.ktx.firestore
    import com.google.firebase.ktx.Firebase
    import kotlinx.coroutines.tasks.await
    import kotlin.math.*
    import androidx.compose.material3.HorizontalDivider
    import android.util.Log
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Warning
    import com.google.android.gms.location.FusedLocationProviderClient
    import com.google.android.gms.location.LocationCallback
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationResult
    import com.google.android.gms.location.Priority
    import android.os.Looper
    import androidx.compose.material3.ExperimentalMaterial3Api
    import com.google.accompanist.permissions.ExperimentalPermissionsApi
    import com.google.accompanist.permissions.*
    import androidx.compose.ui.graphics.Color








    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    @Composable
    fun VerificacionScreen(userViewModel: userViewModel) {
        val context = LocalContext.current
        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
        val locationState = remember { mutableStateOf<LatLng?>(null) }
        val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

        // Solicitud de permisos
        LaunchedEffect(Unit) {
            if (!locationPermissionState.status.isGranted) {
                locationPermissionState.launchPermissionRequest()
            }
        }

        // Obtener ubicación
        LaunchedEffect(locationPermissionState.status) {
            if (locationPermissionState.status.isGranted) {
                val locationRequest = LocationRequest.create().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    maxWaitTime = 15000
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationResult.lastLocation?.let {
                                locationState.value = LatLng(it.latitude, it.longitude)
                                fusedLocationClient.removeLocationUpdates(this)
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            }
        }

        val carro = userViewModel.carro
        val tenenciaPagada = userViewModel.tenenciaPagada
        val verificentros = rememberVerificentros().value

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Verificación Vehicular") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                var openDialog by remember { mutableStateOf(false) }

                FloatingActionButton(onClick = { openDialog = true }) {
                    Text("?")
                }

                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = { openDialog = false },
                        confirmButton = {
                            TextButton(onClick = { openDialog = false }) {
                                Text("Cerrar")
                            }
                        },
                        title = { Text("Ayuda") },
                        text = {
                            Text(
                                "Para verificar tu auto:\n\n" +
                                        "1. Asegúrate de que tu tenencia esté pagada.\n" +
                                        "2. Consulta el mes que te corresponde según tus placas.\n" +
                                        "3. Acude a un verificentro cercano.\n\n" +
                                        "La ubicación actual se usa para mostrar los centros más cercanos."
                            )
                        }
                    )
                }
            }
        )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                carro?.let {
                    val terminacion = it.placas.takeLast(3)
                    val (mes, color) = obtenerMesYColor(terminacion)

                    ElevatedCard(modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF0F0F0))) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("🚗 ${it.marca} ${it.modelo}", style = MaterialTheme.typography.titleLarge)
                            Text("🔢 Placas: ${it.placas}", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📅 Mes de verificación: $mes")
                            Text("🎨 Color: $color")
                        }
                    }
                }

                // Información adicional sobre la verificación
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE0F7FA))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ℹ️ ¿Qué es la verificación?", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Es un proceso obligatorio para verificar que tu vehículo cumple con las normas ambientales. Debes tener la tenencia pagada y acudir a un verificentro en tu periodo correspondiente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

// Detalles adicionales del vehículo (ejemplo: año, tipo)
                carro?.let {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📆 Modelo: ${it.modelo}", style = MaterialTheme.typography.bodyMedium)
                        Text("🚘 Marca: ${it.marca}", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }


                Spacer(modifier = Modifier.height(16.dp))

                when (tenenciaPagada) {
                    true -> {
                        Text("✅ Tenencia pagada. Puedes verificar.", style = MaterialTheme.typography.bodyLarge)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("📍 Verificentros cercanos:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.height(8.dp))

                        locationState.value?.let { userLocation ->
                            val cercanos = verificentros.filter { (pos, _) ->
                                distanciaEnKm(userLocation, pos) < 100
                            }

                            Card(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = rememberCameraPositionState {
                                        position = CameraPosition.fromLatLngZoom(userLocation, 12f)
                                    }
                                ) {
                                    cercanos.forEach { (pos, nombre) ->
                                        Marker(
                                            state = MarkerState(position = pos),
                                            title = nombre
                                        )
                                    }
                                }
                            }
                        } ?: Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Obteniendo ubicación actual...")
                        }
                    }

                    false -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Debes pagar tu tenencia antes de verificar.", color = MaterialTheme.colorScheme.error)
                        }
                    }

                    null -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cargando estado de tenencia...")
                        }
                    }
                }
            }
        }
    }

    fun obtenerMesYColor(terminacion: String): Pair<String, String> {
        return when (terminacion) {
            "5", "6" -> "Julio - Agosto" to "Amarillo"
            "7", "8" -> "Agosto - Septiembre" to "Rosa"
            "3", "4" -> "Septiembre - Octubre" to "Rojo"
            "1", "2" -> "Octubre - Noviembre" to "Verde"
            "9", "0" -> "Noviembre - Diciembre" to "Azul"
            else -> "Desconocido" to "Desconocido"
        }
    }


    fun distanciaEnKm(origen: LatLng, destino: LatLng): Double {
        val radioTierraKm = 6371.0

        val lat1 = Math.toRadians(origen.latitude)
        val lon1 = Math.toRadians(origen.longitude)
        val lat2 = Math.toRadians(destino.latitude)
        val lon2 = Math.toRadians(destino.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2.0) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierraKm * c
    }


    @Composable
    fun rememberVerificentros(): State<List<Pair<LatLng, String>>> {
        val verificentros = remember { mutableStateOf(emptyList<Pair<LatLng, String>>()) }

        LaunchedEffect(Unit) {
            val db = Firebase.firestore
            try {
                val snapshot = db.collection("verificentros").get().await()
                val list = snapshot.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre")
                    val lat = doc.getDouble("lat")
                    val lng = doc.getDouble("lng")
                    if (nombre != null && lat != null && lng != null) {
                        LatLng(lat, lng) to nombre
                    } else null
                }
                verificentros.value = list
                Log.d("VerificacionScreen", "✅ Verificentros cargados: ${list.size}")
            } catch (e: Exception) {
                Log.e("VerificacionScreen", "❌ Error cargando verificentros", e)
            }
        }


        return verificentros
    }

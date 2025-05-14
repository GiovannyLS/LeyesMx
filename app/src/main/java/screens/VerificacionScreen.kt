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





@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerificacionScreen(userViewModel: userViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationState = remember { mutableStateOf<LatLng?>(null) }


    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 15000
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                        val location = locationResult.lastLocation
                        location?.let {
                            locationState.value = LatLng(it.latitude, it.longitude)
                            println("📍 Ubicación actualizada: ${it.latitude}, ${it.longitude}")
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }
                },
                android.os.Looper.getMainLooper()
            )
        }
    }




    val carro = userViewModel.carro
    val tenenciaPagada = userViewModel.tenenciaPagada
    val verificentros = rememberVerificentros().value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Verificación Vehicular", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (carro != null) {
            val terminacion = carro.placas.takeLast(3)
            val (mes, color) = obtenerMesYColor(terminacion)
            Text("🚗 Vehículo: ${carro.marca} ${carro.modelo} - ${carro.placas}")
            Text("📅 Verifica en: $mes (Color: $color)")
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        when (tenenciaPagada) {
            true -> {
                Text("✅ Tenencia pagada. Puedes verificar.")
                Text("📍 Verificentros cercanos:")

                Spacer(modifier = Modifier.height(8.dp))

                locationState.value?.let { userLocation ->
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {

                        val cercanos = verificentros.filter { (pos, _) ->
                            val distancia = distanciaEnKm(userLocation, pos)
                            Log.d("VerificacionScreen", "📌 Verificentros distancia: $distancia km")
                            distancia < 100
                        }
                        Log.d("VerificacionScreen", "🎯 Verificentros cercanos encontrados: ${cercanos.size}")


                        GoogleMap(
                            modifier = Modifier.matchParentSize(),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(userLocation, 12f)
                            }
                        ) {
                            Marker(
                                state = MarkerState(position = userLocation),
                                title = "Tú estás aquí",
                                snippet = "Ubicación actual"
                            )

                            cercanos.forEach { (pos, nombre) ->
                                Marker(
                                    state = MarkerState(position = pos),
                                    title = nombre
                                )
                            }

                        }
                    }
                } ?: Text("⏳ Obteniendo ubicación actual...")
            }
            false -> Text("❌ Debes pagar tu tenencia antes de verificar.", color = MaterialTheme.colorScheme.error)
            null -> Text("⏳ Cargando estado de tenencia...")
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

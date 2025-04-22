package com.example.leyesmx
import com.example.leyesmx.screens.NoticiasScreen
import com.example.leyesmx.ui.screens.InfoPantalla
import com.example.leyesmx.viewmodel.NoticiasViewModel
import com.example.leyesmx.data.NoticiasApi
import com.example.leyesmx.repository.NoticiasRepository


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.leyesmx.ui.theme.LeyesMxTheme
import androidx.navigation.NavGraphBuilder
import androidx.compose.animation.*
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.animation.AnimatedNavHost
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeyesMxTheme {
                LeyesMxApp()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LeyesMxApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo LeyesMX",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )
                        Text("LeyesMX", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
        ) {
            composable("menu") { MainMenu(navController) }
            composable("constitucion") { Pantalla("Constitución Mexicana") }
            composable("transito") { Pantalla("Reglamento de Tránsito") }
            composable("tenencia") {
                InfoPantalla(
                    titulo = "Pago de Tenencia",
                    descripcion = "La tenencia es un impuesto que se paga anualmente por el uso de vehículos. Se puede pagar en línea o en las oficinas de finanzas de tu estado.",
                    icon = painterResource(id = R.drawable.ic_tenencia)
                )
            }

            composable("verificacion") {
                InfoPantalla(
                    titulo = "Verificación Vehicular",
                    descripcion = "La verificación es obligatoria para reducir emisiones contaminantes. Consulta tu calendario y verifica tu vehículo según tu terminación de placa.",
                    icon = painterResource(id = R.drawable.ic_verificacion)
                )
            }

            composable("multas") {
                InfoPantalla(
                    titulo = "Multas",
                    descripcion = "Consulta si tienes multas pendientes y realiza su pago a tiempo para evitar recargos. Puedes hacerlo en línea o en ventanilla.",
                    icon = painterResource(id = R.drawable.ic_multas)
                )
            }
            composable("noticias") {
                val viewModel = remember {
                    NoticiasViewModel(
                        NoticiasRepository(
                            Retrofit.Builder()
                                .baseUrl("https://newsapi.org/v2/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(NoticiasApi::class.java),
                            apiKey = "TU_API_KEY_AQUÍ"
                        )
                    )
                }
                NoticiasScreen(viewModel)
            }
        }
    }
}
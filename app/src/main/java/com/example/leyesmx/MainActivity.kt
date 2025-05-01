package com.example.leyesmx
import com.example.leyesmx.screens.NoticiasScreen
import com.example.leyesmx.ui.screens.InfoPantalla
import com.example.leyesmx.viewmodel.NoticiasViewModel
import com.example.leyesmx.data.RetrofitClient
import com.example.leyesmx.repository.NoticiasRepository
import com.example.leyesmx.screens.ConstitucionScreen
import com.example.leyesmx.viewmodel.ConstitucionViewModel
import com.example.leyesmx.viewmodel.TransitoViewModel
import com.example.leyesmx.screens.TransitoScreen
import com.example.leyesmx.screens.RegistroScreen
import com.example.leyesmx.screens.RegistroCarroScreen
import com.example.leyesmx.screens.PerfilUsuarioScreen
import com.example.leyesmx.screens.VerCarroScreen
import com.example.leyesmx.screens.MainMenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leyesmx.ui.theme.LeyesMxTheme
import androidx.compose.animation.*
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.animation.AnimatedNavHost
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.example.leyesmx.R

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Opciones", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                NavigationDrawerItem(
                    label = { Text("Perfil de Usuario") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("perfil_usuario")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ver Carro") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("ver_carro")
                    }
                )
            }
        }
    ) {
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
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
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
                composable("constitucion") {
                    ConstitucionScreen(viewModel = ConstitucionViewModel())
                }
                composable("transito") {

                    TransitoScreen(viewModel = TransitoViewModel())
                }
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
                                RetrofitClient.apiService,
                                apiKey = "5d75ab6751834c229230793d07cc17e5"
                            )
                        )
                    }
                    NoticiasScreen(viewModel)
                }

            }
        }
    }
}



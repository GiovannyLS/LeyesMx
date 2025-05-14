package com.example.leyesmx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.leyesmx.data.RetrofitClient
import com.example.leyesmx.repository.NoticiasRepository
import com.example.leyesmx.screens.*
import com.example.leyesmx.ui.screens.InfoPantalla
import com.example.leyesmx.ui.theme.LeyesMxTheme
import com.example.leyesmx.viewmodel.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
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
    val userViewModel: userViewModel = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Opciones", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)

                if (userViewModel.usuario == null) {
                    NavigationDrawerItem(
                        label = { Text("Iniciar Sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("login")
                        }
                    )
                } else {
                    NavigationDrawerItem(
                        label = { Text("Mi Cuenta: ${userViewModel.usuario?.nombre}") },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        label = { Text("Ver Carro") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("ver_carro")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Cerrar Sesión") },
                        selected = false,
                        onClick = {
                            userViewModel.logout()
                            scope.launch { drawerState.close() }
                            navController.navigate("menu")
                        }
                    )
                }
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
                composable("menu") { MainMenu(navController, userViewModel) }

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
                    VerificacionScreen(userViewModel)
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

                // Login y gestión de usuario/carro
                composable("login") {
                    LoginScreen(navController, userViewModel)
                }

                composable("registro_carro") {
                    RegistroCarroScreen(userViewModel, navController)
                }

                composable("ver_carro") {
                    VerCarroScreen(userViewModel, navController)
                }

                composable("registro_usuario") {
                    RegistroUsuarioScreen(navController)
                }
            }
        }
    }
}
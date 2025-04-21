package com.example.leyesmx

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
            composable("multas") { Pantalla("Multas") }
            composable("verificacion") { Pantalla("Verificación Vehicular") }
            composable("tenencia") { Pantalla("Pago de Tenencia") }
            composable("noticias") { Pantalla("Noticias Legales") }
        }
    }
}
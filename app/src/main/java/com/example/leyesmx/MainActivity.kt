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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeyesMxApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LeyesMX") }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(innerPadding)
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
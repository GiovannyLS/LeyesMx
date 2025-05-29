import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.leyesmx.viewmodel.userViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun MiCuentaScreen(userViewModel: userViewModel, navController: NavController) {
    val usuario = userViewModel.usuario
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Cuenta", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = usuario?.email ?: "",
            onValueChange = {},
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (uid != null) {
                    cargando = true
                    db.collection("usuarios").document(uid)
                        .update("nombre", nombre)
                        .addOnSuccessListener {
                            userViewModel.actualizarNombre(nombre)
                            mensaje = "Datos actualizados correctamente"
                            cargando = false
                        }
                        .addOnFailureListener {
                            mensaje = "Error al actualizar: ${it.message}"
                            cargando = false
                        }
                }
            },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardando...")
            } else {
                Text("Guardar cambios")
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}

package com.example.leyesmx.auth

import coil.compose.AsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import java.lang.Error

class FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun registrarUsuario(
        correo: String,
        contraseña: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onSuccess()
                }else{
                    onError(task.exception?.message ?: "Error Desconocido")
                }
            }
    }

    fun obtenerUsuarioId(): String? {
        return auth.currentUser?.uid
    }
}
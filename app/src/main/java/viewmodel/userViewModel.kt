package com.example.leyesmx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.leyesmx.model.Usuario
import com.example.leyesmx.model.Carro

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth


class userViewModel : ViewModel() {
    var usuario: Usuario? by mutableStateOf(null)
    var carro: Carro? by mutableStateOf(null)

    fun login(uid: String, nombre: String, email: String, carro: Carro? = null) {
        usuario = Usuario(
            uid = uid,
            nombre = nombre,
            email = email,
            carro = carro
        )
    }

    fun registrarCarro(marca: String, modelo: String, placas: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val carroInfo = Carro(marca, modelo, placas)

            // Guardar en Firestore
            val data = hashMapOf(
                "marca" to carroInfo.marca,
                "modelo" to carroInfo.modelo,
                "placas" to carroInfo.placas
            )

            FirebaseFirestore.getInstance()
                .collection("carros")
                .document(uid)
                .set(data)

            // Actualizar el estado local
            carro = carroInfo

            // Si ya hay un usuario logueado, actualizamos su carro también
            usuario = usuario?.copy(carro = carroInfo)
        }
    }

    fun logout() {
        usuario = null
        carro = null
    }

    fun setUsuario(uid: String, nombre: String, email: String, carro: Carro?) {
        usuario = Usuario(uid = uid, nombre = nombre, email = email, carro = carro)
    }
}
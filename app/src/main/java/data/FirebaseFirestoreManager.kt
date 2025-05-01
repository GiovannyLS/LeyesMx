package com.example.leyesmx.data

import okhttp3.internal.platform.BouncyCastlePlatform
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    fun guardarPerfil (userId: String, nombre: String, edad: Int, ciudad: String){

        val perfil = hashMapOf(
            "nombre" to nombre,
            "edad" to edad,
            "ciudad" to ciudad
        )
        db.collection("usuarios").document(userId).set(perfil)
    }

    fun guardarCarro(userId: String, marca: String, modelo: String, placas: String){

        val carro = hashMapOf(
            "marca" to marca,
            "modelo" to modelo,
            "placas" to placas
        )
        db.collection("usuarios").document(userId)
            .collection("carros").add(carro)
    }
}
package com.example.leyesmx.model

data class Usuario(
    val uid: String,
    val nombre: String,
    val email: String,
    val password: String? = null,
    val carro: Carro? = null
)

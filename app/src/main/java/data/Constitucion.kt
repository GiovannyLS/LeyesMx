package com.example.leyesmx.data

import com.example.leyesmx.model.Articulo
import retrofit2.http.GET

interface ConstitucionApi {
    @GET("constitucion.json")
    suspend fun obtenerArticulos(): List<Articulo>
}
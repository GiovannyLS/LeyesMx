package com.example.leyesmx.data

import com.example.leyesmx.model.ArticuloTransito
import retrofit2.http.GET

interface TransitoApi{
    @GET("transito.json")
    suspend fun obtenerArticulosTransito(): List<ArticuloTransito>
}
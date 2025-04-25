package com.example.leyesmx.repository

import com.example.leyesmx.data.ConstitucionApi
import com.example.leyesmx.data.ConstitucionRetrofitClient
import com.example.leyesmx.model.Articulo

class ConstitucionRepository {
    private val api = ConstitucionRetrofitClient.retrofit.create(ConstitucionApi::class.java)

    suspend fun obtenerArticulos(): List<Articulo> {
        return api.obtenerArticulos()
    }
}
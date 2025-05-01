package com.example.leyesmx.repository

import com.example.leyesmx.data.TransitoRetrofitClient
import com.example.leyesmx.model.ArticuloTransito

class TransitoRepository{

    suspend fun obtenerArticulosTransito(): List<ArticuloTransito>{
        return  TransitoRetrofitClient.api.obtenerArticulosTransito()
    }
}
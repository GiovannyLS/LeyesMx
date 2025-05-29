package com.example.leyesmx.data

import model.MercadoLibreResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MercadoLibreApi{

    @GET("sites/MLM/search")
    suspend fun searchProducts(@Query("q") query: String): MercadoLibreResponse
}
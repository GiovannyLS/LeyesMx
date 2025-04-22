package com.example.leyesmx.data

import com.example.leyesmx.model.NoticiasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticiasApi {
    @GET("top-headlines")
    suspend fun obtenerNoticias(
        @Query("country") country: String = "mx",
        @Query("category") category: String = "general",
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 10
    ): NoticiasResponse
}
package com.example.leyesmx.data

import com.example.leyesmx.model.NoticiasResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NoticiasApi {
    @GET("top-headlines")
    suspend fun obtenerNoticias(
        @Query("country") country: String = "us",
        @Query("category") category: String = "technology",
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 10
    ): NoticiasResponse
}
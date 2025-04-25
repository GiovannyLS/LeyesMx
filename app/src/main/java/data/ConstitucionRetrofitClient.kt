package com.example.leyesmx.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConstitucionRetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/GiovannyLS/constitucion-api/main/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
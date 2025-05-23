package com.example.leyesmx.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TransitoRetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/GiovannyLS/constitucion-api/main/"

    val api: TransitoApi by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TransitoApi::class.java)
    }

}
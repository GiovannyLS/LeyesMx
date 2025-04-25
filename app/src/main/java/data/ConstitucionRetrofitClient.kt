package com.example.leyesmx.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConstitucionRetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/GiovannyLS/"

    val api: ConstitucionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConstitucionApi::class.java)
    }
}
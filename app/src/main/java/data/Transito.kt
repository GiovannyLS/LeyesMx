package com.example.leyesmx.data

import com.example.leyesmx.model.ReglamentoEstado
import retrofit2.http.GET

    interface TransitoApi {
        @GET("transito.json")
        suspend fun obtenerReglamento(): ReglamentoEstado
    }

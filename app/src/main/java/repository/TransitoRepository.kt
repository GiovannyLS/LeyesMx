package com.example.leyesmx.repository

import com.example.leyesmx.data.TransitoRetrofitClient
import com.example.leyesmx.model.ReglamentoEstado


class TransitoRepository {

    suspend fun obtenerReglamento(): ReglamentoEstado {
        return TransitoRetrofitClient.api.obtenerReglamento()
    }

    }

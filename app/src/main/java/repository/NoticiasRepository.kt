package com.example.leyesmx.repository

import com.example.leyesmx.model.Noticia
import com.example.leyesmx.data.NoticiasApi

class NoticiasRepository(
    private val api: NoticiasApi,
    private val apiKey: String
) {
    suspend fun obtenerNoticias(page: Int): List<Noticia> {
        return api.obtenerNoticias(apiKey = apiKey, page = page).articles
    }
}
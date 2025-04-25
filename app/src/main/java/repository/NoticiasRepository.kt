package com.example.leyesmx.repository

import android.util.Log
import com.example.leyesmx.model.Noticia
import com.example.leyesmx.data.NoticiasApi
import com.example.leyesmx.model.Article

class NoticiasRepository(
    private val api: NoticiasApi,
    private val apiKey: String
) {
    suspend fun obtenerNoticias(page: Int): List<Noticia> {
        val response = api.obtenerNoticias(apiKey = apiKey, page = page)
        Log.d("NoticiasRepository", "Artículos recibidos: ${response.articles.size}")
        return response.articles.map { article ->
            Noticia(
                title = article.title ?: "Sin título",
                url = article.url ?: "",
                urlToImage = article.urlToImage
            )
        }
    }


    suspend fun buscarNoticiasPorPalabraClave(query: String, page: Int): List<Noticia> {
        val response = api.buscarNoticias(query = query, apiKey = apiKey, page = page)
        return response.articles.map { article ->
            Noticia(
                title = article.title ?: "Sin título",
                url = article.url ?: "",
                urlToImage = article.urlToImage
            )
        }
    }




}
package com.example.leyesmx.model

data class Noticia(
    val title: String,
    val url: String,
    val urlToImage: String?
)

data class NoticiasResponse(
    val articles: List<Noticia>
)
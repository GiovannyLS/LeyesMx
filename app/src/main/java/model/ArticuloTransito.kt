package com.example.leyesmx.model

data class ArticuloTransito (
    val titulo: String,
    val contenido: String,
    val descripcionInfraccion: String,
    val fracciones: List<Fraccion> = emptyList()
)

data class ReglamentoEstado(
    val nombre: String,
    val titulos: List<Titulo>
)
data class Titulo (
    val nombre: String,
    val capitulos: List<Capitulo>
)
data class Capitulo(
    val nombre: String,
    val articulos: List<ArticuloTransito>
)
data class Fraccion(
    val numero: String,
    val texto: String,
    val descripcionInfraccion: String,
)

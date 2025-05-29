package model

data class MercadoLibreResponse(
    val results: List<MercadoLibreItem>
)

data class MercadoLibreItem(
    val id: String,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val permalink: String
)
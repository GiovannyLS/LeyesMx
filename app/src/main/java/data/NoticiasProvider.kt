package com.example.leyesmx.data

import com.example.leyesmx.model.Noticia

object NoticiasProvider {
    val noticias = listOf(
        Noticia(
            1,
            "Reforma constitucional aprobada",
            "El Senado aprobó una reforma constitucional en materia de justicia.",
            "El Universal",
            "20 de abril de 2025"
        ),
        Noticia(
            2,
            "Nueva ley de movilidad",
            "Se publicó en el DOF una nueva ley que regula el tránsito en zonas urbanas.",
            "DOF",
            "18 de abril de 2025"
        ),
        Noticia(
            3,
            "Incrementan multas por contaminación",
            "Los vehículos sin verificación serán multados hasta con 2,000 MXN.",
            "Milenio",
            "17 de abril de 2025"
        )
    )
}
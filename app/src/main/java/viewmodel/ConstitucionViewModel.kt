package com.example.leyesmx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leyesmx.data.ConstitucionRetrofitClient
import com.example.leyesmx.model.Articulo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConstitucionViewModel : ViewModel() {

    private val _articulos = MutableStateFlow<List<Articulo>>(emptyList())
    val articulos: StateFlow<List<Articulo>> = _articulos

    init {
        obtenerArticulos()
    }

    fun obtenerArticulos() {
        viewModelScope.launch {
            try {
                val resultado = ConstitucionRetrofitClient.api.obtenerArticulos()
                _articulos.value = resultado
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
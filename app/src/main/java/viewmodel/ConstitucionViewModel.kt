package com.example.leyesmx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leyesmx.model.Articulo
import com.example.leyesmx.repository.ConstitucionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConstitucionViewModel : ViewModel() {

    private val repository = ConstitucionRepository()

    private val _articulos = MutableStateFlow<List<Articulo>>(emptyList())
    val articulos: StateFlow<List<Articulo>> = _articulos

    init {
        cargarArticulos()
    }

    fun cargarArticulos() {
        viewModelScope.launch {
            try {
                val lista = repository.obtenerArticulos()
                _articulos.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
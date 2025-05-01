package com.example.leyesmx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.leyesmx.model.ArticuloTransito
import com.example.leyesmx.repository.TransitoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransitoViewModel : ViewModel() {

    private val repository = TransitoRepository()

    private val _articulos = MutableStateFlow<List<ArticuloTransito>>(emptyList())
    val articulos: StateFlow<List<ArticuloTransito>> = _articulos

    init {
        obtenerArticulosTransito()
    }

    private fun obtenerArticulosTransito() {

        viewModelScope.launch {
            try {
                _articulos.value = repository.obtenerArticulosTransito()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}
package com.example.leyesmx.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leyesmx.data.RetrofitInstance
import kotlinx.coroutines.launch
import model.MercadoLibreItem
import model.MercadoLibreResponse

class TiendaViewModel : ViewModel() {
    var productos by mutableStateOf<List<MercadoLibreItem>>(emptyList())
    var loading by mutableStateOf(false)

    fun buscarRefaccion(query: String) {
        viewModelScope.launch {
            loading = true
            try {
                val response = RetrofitInstance.api.searchProducts(query)
                productos = response.results
            } catch (e: Exception) {
                productos = emptyList()
            }
            loading = false
        }
    }
}
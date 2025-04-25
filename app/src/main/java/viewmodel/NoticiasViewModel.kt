package com.example.leyesmx.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leyesmx.model.Noticia
import com.example.leyesmx.repository.NoticiasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoticiasViewModel(
    private val repository: NoticiasRepository
) : ViewModel() {

    private val _noticias = MutableStateFlow<List<Noticia>>(emptyList())
    val noticias: StateFlow<List<Noticia>> = _noticias

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var currentPage = 1
    private var isSearching = false

    init {
        cargarNoticias()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        buscarNoticias(query)
    }

    fun cargarNoticias() {
        if (isSearching) return // No cargar más si estamos en búsqueda

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevasNoticias = repository.obtenerNoticias(currentPage)
                _noticias.value += nuevasNoticias
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("NoticiasViewModel", "Error cargando noticias", e)
            }
            _isLoading.value = false
        }
    }

    fun refrescar() {
        currentPage = 1
        _noticias.value = emptyList()
        if (_searchQuery.value.isEmpty()) {
            cargarNoticias()
        } else {
            buscarNoticias(_searchQuery.value)
        }
    }

    fun buscarNoticias(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            isSearching = query.isNotEmpty()

            try {
                val resultado = if (query.isNotEmpty()) {
                    repository.buscarNoticiasPorPalabraClave(query, 1)
                } else {
                    repository.obtenerNoticias(1)
                }
                _noticias.value = resultado
                currentPage = 2
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("NoticiasViewModel", "Error buscando noticias", e)
            }
            _isLoading.value = false
        }
    }
}
package com.example.leyesmx.viewmodel

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

    private var currentPage = 1

    init {
        cargarNoticias()
    }

    fun cargarNoticias() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevasNoticias = repository.obtenerNoticias(currentPage)
                _noticias.value += nuevasNoticias
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }

    fun refrescar() {
        currentPage = 1
        _noticias.value = emptyList()
        cargarNoticias()
    }
}
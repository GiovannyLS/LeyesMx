package com.example.leyesmx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leyesmx.model.ReglamentoEstado
import com.example.leyesmx.repository.TransitoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransitoViewModel : ViewModel() {

    private val repository = TransitoRepository()

    private val _reglamento = MutableStateFlow<ReglamentoEstado?>(null)
    val reglamento: StateFlow<ReglamentoEstado?> = _reglamento

    init {
        obtenerReglamento()
    }

    private fun obtenerReglamento() {
        viewModelScope.launch {
            try {
                _reglamento.value = repository.obtenerReglamento()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

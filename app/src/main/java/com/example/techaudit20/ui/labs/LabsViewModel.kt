package com.example.techaudit20.ui.labs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.data.repository.TechAuditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LabsViewModel(
    private val repo: TechAuditRepository
) : ViewModel() {

    val labs = repo.labsFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun addLab(nombre: String, edificio: String) {
        val n = nombre.trim()
        val e = edificio.trim()

        if (n.isBlank() || e.isBlank()) {
            _message.value = "Campos vacíos"
            return
        }

        viewModelScope.launch {
            repo.addLab(n, e)
            _message.value = "Laboratorio creado"
        }
    }

    fun sync() {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repo.sync()
            _isSyncing.value = false

            _message.value =
                if (result.isSuccess) "Sincronización exitosa"
                else "Error: ${result.exceptionOrNull()?.message}"
        }
    }
}
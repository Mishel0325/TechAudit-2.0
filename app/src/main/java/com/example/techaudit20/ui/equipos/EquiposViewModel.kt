package com.example.techaudit20.ui.equipos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.data.local.EstadoEquipo
import com.example.techaudit20.data.local.EquipoEntity
import com.example.techaudit20.data.repository.TechAuditRepository
import kotlinx.coroutines.launch

class EquiposViewModel(
    private val labId: Long,
    private val repo: TechAuditRepository
) : ViewModel() {

    val equipos = repo.equiposFlow(labId)

    fun addEquipo(nombre: String, estado: EstadoEquipo) {
        val n = nombre.trim()
        if (n.isBlank()) return

        viewModelScope.launch {
            repo.addEquipo(labId, n, estado)
        }
    }

    fun deleteEquipo(item: EquipoEntity) {
        viewModelScope.launch {
            repo.deleteEquipo(item)
        }
    }

    fun updateEquipo(item: EquipoEntity) {
        val n = item.nombre.trim()
        if (n.isBlank()) return

        viewModelScope.launch {
            repo.updateEquipo(item.copy(nombre = n))
        }
    }
}
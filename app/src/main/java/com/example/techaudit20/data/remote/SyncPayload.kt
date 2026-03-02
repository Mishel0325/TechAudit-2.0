package com.example.techaudit20.data.remote

import com.example.techaudit20.data.local.EquipoEntity
import com.example.techaudit20.data.local.LaboratorioEntity

data class SyncPayload(
    val laboratorios: List<LaboratorioEntity>,
    val equipos: List<EquipoEntity>
)
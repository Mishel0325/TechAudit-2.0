package com.example.techaudit20.data.repository

import com.example.techaudit20.data.local.*
import com.example.techaudit20.data.remote.SyncPayload
import com.example.techaudit20.data.remote.TechAuditApi

class TechAuditRepository(
    private val labDao: LaboratorioDao,
    private val eqDao: EquipoDao,
    private val api: TechAuditApi
) {
    // Flows para UI
    fun labsFlow() = labDao.getAll()
    fun equiposFlow(labId: Long) = eqDao.getByLaboratorio(labId)

    // CRUD Laboratorios
    suspend fun addLab(nombre: String, edificio: String): Long {
        return labDao.insert(
            LaboratorioEntity(
                nombre = nombre.trim(),
                edificio = edificio.trim()
            )
        )
    }

    suspend fun deleteLab(lab: LaboratorioEntity) {
        labDao.delete(lab)
    }

    suspend fun updateLab(lab: LaboratorioEntity) {
        labDao.update(lab)
    }

    // CRUD Equipos
    suspend fun addEquipo(labId: Long, nombre: String, estado: EstadoEquipo): Long {
        return eqDao.insert(
            EquipoEntity(
                nombre = nombre.trim(),
                estado = estado,
                laboratorioId = labId
            )
        )
    }

    suspend fun deleteEquipo(eq: EquipoEntity) {
        eqDao.delete(eq)
    }

    suspend fun updateEquipo(eq: EquipoEntity) {
        eqDao.update(eq)
    }

    // Sync a MockAPI
    suspend fun sync(): Result<Unit> {
        return try {
            // Traer todo desde Room
            val labs = eqDao.getLabsOnce()
            val equipos = eqDao.getEquiposOnce()

            val payload = SyncPayload(
                laboratorios = labs,
                equipos = equipos
            )

            val response = api.postSync(payload)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
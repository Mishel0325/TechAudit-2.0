package com.example.techaudit20.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {

    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId ORDER BY id DESC")
    fun getByLaboratorio(labId: Long): Flow<List<EquipoEntity>>

    @Insert
    suspend fun insert(item: EquipoEntity): Long

    @Update
    suspend fun update(item: EquipoEntity)

    @Delete
    suspend fun delete(item: EquipoEntity)

    // Para sincronizar (leer todo)
    @Query("SELECT * FROM laboratorios")
    suspend fun getLabsOnce(): List<LaboratorioEntity>

    @Query("SELECT * FROM equipos")
    suspend fun getEquiposOnce(): List<EquipoEntity>
}
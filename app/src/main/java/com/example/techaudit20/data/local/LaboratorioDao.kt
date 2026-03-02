package com.example.techaudit20.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LaboratorioDao {

    @Query("SELECT * FROM laboratorios ORDER BY id DESC")
    fun getAll(): Flow<List<LaboratorioEntity>>

    @Insert
    suspend fun insert(item: LaboratorioEntity): Long

    @Update
    suspend fun update(item: LaboratorioEntity)

    @Delete
    suspend fun delete(item: LaboratorioEntity)
}
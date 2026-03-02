package com.example.techaudit20.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laboratorios")
data class LaboratorioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val edificio: String
)
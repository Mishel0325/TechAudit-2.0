package com.example.techaudit20.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = LaboratorioEntity::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("laboratorioId")]
)
data class EquipoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val estado: EstadoEquipo,
    val laboratorioId: Long
)
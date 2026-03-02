package com.example.techaudit20.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [LaboratorioEntity::class, EquipoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun laboratorioDao(): LaboratorioDao
    abstract fun equipoDao(): EquipoDao
}
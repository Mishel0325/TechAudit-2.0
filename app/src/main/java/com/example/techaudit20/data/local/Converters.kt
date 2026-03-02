package com.example.techaudit20.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun toEstado(value: String): EstadoEquipo = EstadoEquipo.valueOf(value)

    @TypeConverter
    fun fromEstado(value: EstadoEquipo): String = value.name
}
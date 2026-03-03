package com.example.techaudit20

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class EquiposActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LAB_ID = "LAB_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtener el ID del laboratorio enviado desde MainActivity
        val labId = intent.getIntExtra(EXTRA_LAB_ID, -1)

        // Vista simple para mostrar el ID del laboratorio
        val tv = TextView(this)
        tv.textSize = 22f
        tv.text = "Equipos del laboratorio ID: $labId"

        setContentView(tv)
    }
}
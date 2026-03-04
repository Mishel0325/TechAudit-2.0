package com.example.techaudit20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.data.local.DbProvider
import com.example.techaudit20.data.local.LaboratorioEntity
import com.example.techaudit20.data.remote.ApiProvider
import com.example.techaudit20.data.repository.TechAuditRepository
import com.example.techaudit20.ui.equipos.EquiposActivity
import com.example.techaudit20.ui.labs.LaboratorioAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repo: TechAuditRepository
    private lateinit var rvLabs: RecyclerView
    private lateinit var tvMsg: TextView
    private lateinit var pbSync: ProgressBar

    private val labsList = mutableListOf<LaboratorioEntity>()
    private lateinit var labsAdapter: LaboratorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Views
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etEdificio = findViewById<TextInputEditText>(R.id.etEdificio)
        val btnCrear = findViewById<Button>(R.id.btnCrearLab)
        val btnSync = findViewById<Button>(R.id.btnSync)
        pbSync = findViewById(R.id.pbSync)
        tvMsg = findViewById(R.id.tvMsg)
        rvLabs = findViewById(R.id.rvLabs)

        // Adapter + RecyclerView
        labsAdapter = LaboratorioAdapter(labsList) { lab ->
            val intent = Intent(this, EquiposActivity::class.java)
            intent.putExtra(EquiposActivity.EXTRA_LAB_ID, lab.id) // lab.id es Long ✅
            startActivity(intent)
        }
        rvLabs.layoutManager = LinearLayoutManager(this)
        rvLabs.adapter = labsAdapter

        // DB + API
        val db = DbProvider.get(this)
        val api = ApiProvider.create("https://69a5f58a885dcb6bd6a9b9db.mockapi.io/")

        // Repository
        repo = TechAuditRepository(
            db.laboratorioDao(),
            db.equipoDao(),
            api
        )

        // Cargar labs en RV
        lifecycleScope.launch {
            repo.labsFlow().collect { labs ->
                labsAdapter.submitList(labs)
            }
        }

        // Crear laboratorio
        btnCrear.setOnClickListener {
            val nombre = etNombre.text?.toString().orEmpty().trim()
            val edificio = etEdificio.text?.toString().orEmpty().trim()

            lifecycleScope.launch {
                repo.addLab(nombre, edificio)
                tvMsg.text = "Laboratorio creado"
            }

            etNombre.setText("")
            etEdificio.setText("")
        }

        // Sync
        btnSync.setOnClickListener {
            lifecycleScope.launch {
                pbSync.visibility = View.VISIBLE
                val result = repo.sync()
                pbSync.visibility = View.GONE

                tvMsg.text =
                    if (result.isSuccess) "Sincronización exitosa"
                    else "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }
}
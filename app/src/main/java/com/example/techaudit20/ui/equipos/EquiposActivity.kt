package com.example.techaudit20.ui.equipos

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.R
import com.example.techaudit20.data.local.DbProvider
import com.example.techaudit20.data.local.EstadoEquipo
import com.example.techaudit20.data.local.EquipoEntity
import com.example.techaudit20.data.remote.ApiProvider
import com.example.techaudit20.data.repository.TechAuditRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class EquiposActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LAB_ID = "LAB_ID"
    }

    private lateinit var viewModel: EquiposViewModel
    private lateinit var adapter: EquipoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_equipos)

        // Obtener labId
        val labId = intent.getLongExtra(EXTRA_LAB_ID, -1L)

        // Views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val etNombre = findViewById<TextInputEditText>(R.id.etNombreEquipo)
        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val btnAgregar = findViewById<Button>(R.id.btnAgregarEquipo)
        val tvMsg = findViewById<TextView>(R.id.tvMsgEquipos)
        val rvEquipos = findViewById<RecyclerView>(R.id.rvEquipos)

        // Spinner estados (crear)
        val estados = EstadoEquipo.values().map { it.name }
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = spinnerAdapter

        // DB + API
        val db = DbProvider.get(this)
        val api = ApiProvider.create("https://69a5f58a885dcb6bd6a9b9db.mockapi.io/")

        val repo = TechAuditRepository(
            db.laboratorioDao(),
            db.equipoDao(),
            api
        )

        // ViewModel
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EquiposViewModel(labId, repo) as T
            }
        })[EquiposViewModel::class.java]

        // RecyclerView + Adapter (CLICK = EDITAR)
        adapter = EquipoAdapter { item ->
            showEditDialog(item, tvMsg)
        }
        rvEquipos.layoutManager = LinearLayoutManager(this)
        rvEquipos.adapter = adapter

        // Observa equipos
        lifecycleScope.launch {
            viewModel.equipos.collect { list ->
                adapter.submitList(list)
            }
        }

        // Agregar equipo
        btnAgregar.setOnClickListener {
            val nombre = etNombre.text?.toString().orEmpty()
            val estado = EstadoEquipo.valueOf(spEstado.selectedItem.toString())

            viewModel.addEquipo(nombre, estado)
            tvMsg.text = "Equipo agregado"
            etNombre.setText("")
        }

        // Swipe para eliminar
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.getItemAt(viewHolder.adapterPosition)
                viewModel.deleteEquipo(item)
                tvMsg.text = "Equipo eliminado"
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(rvEquipos)

        // Volver
        btnBack.setOnClickListener { finish() }
    }

    private fun showEditDialog(item: EquipoEntity, tvMsg: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_equipo, null)

        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etEditNombreEquipo)
        val spEstado = dialogView.findViewById<Spinner>(R.id.spEditEstado)

        etNombre.setText(item.nombre)

        val estados = EstadoEquipo.values().map { it.name }
        val spAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstado.adapter = spAdapter

        val idx = estados.indexOf(item.estado.name).coerceAtLeast(0)
        spEstado.setSelection(idx)

        AlertDialog.Builder(this)
            .setTitle("Editar equipo")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = etNombre.text?.toString().orEmpty()
                val nuevoEstado = EstadoEquipo.valueOf(spEstado.selectedItem.toString())

                viewModel.updateEquipo(
                    item.copy(nombre = nuevoNombre, estado = nuevoEstado)
                )
                tvMsg.text = "Equipo actualizado"
            }
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .show()
    }
}
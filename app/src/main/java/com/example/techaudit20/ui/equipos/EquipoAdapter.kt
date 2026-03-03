package com.example.techaudit20.ui.equipos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.R
import com.example.techaudit20.data.local.EquipoEntity

class EquipoAdapter : RecyclerView.Adapter<EquipoAdapter.VH>() {

    private val items = mutableListOf<EquipoEntity>()

    fun submitList(newItems: List<EquipoEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): EquipoEntity = items[position]

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEquipo)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoEquipo)
        private val tvId: TextView = itemView.findViewById(R.id.tvIdEquipo)

        fun bind(item: EquipoEntity) {
            tvNombre.text = item.nombre
            tvEstado.text = "Estado: ${item.estado.name}"
            tvId.text = "ID: ${item.id}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
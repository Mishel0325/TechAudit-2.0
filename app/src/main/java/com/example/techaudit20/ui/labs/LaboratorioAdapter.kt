package com.example.techaudit20.ui.labs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techaudit20.R
import com.example.techaudit20.data.local.LaboratorioEntity

class LaboratorioAdapter(
    private val items: MutableList<LaboratorioEntity>,
    private val onClick: (LaboratorioEntity) -> Unit
) : RecyclerView.Adapter<LaboratorioAdapter.LaboratorioViewHolder>() {

    fun submitList(newItems: List<LaboratorioEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class LaboratorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreLab)
        private val tvEdificio: TextView = itemView.findViewById(R.id.tvEdificioLab)
        private val tvId: TextView = itemView.findViewById(R.id.tvIdLab)

        fun bind(item: LaboratorioEntity) {
            tvNombre.text = item.nombre
            tvEdificio.text = "Edificio: ${item.edificio}"
            tvId.text = "ID: ${item.id}"
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaboratorioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laboratorio, parent, false)
        return LaboratorioViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaboratorioViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
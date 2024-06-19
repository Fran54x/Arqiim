package com.example.proyectofinal_moviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView

class HistorialAdapter(private val items: List<producto>) : RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nombreProducto.text = item.nombre
        holder.precioPorDia.text = "Precio al Día: \$${item.precio}"
        holder.diasARentar.text = "Días a rentar: ${item.diasARentar}"
        val precio = item.precio.toDoubleOrNull() ?: 0.0
        val total = precio * item.diasARentar
        holder.imagenProducto.setImageResource(item.imagen)
        holder.total.text = "Total: \$${item.precioTotal}"
        holder.fechaInicio.text = "Desde: ${item.fechaInicio}"
        holder.fechaFin.text = "Hasta: ${item.fechaFin}"

    }


    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreProducto: TextView = view.findViewById(R.id.nombre_producto)
        val precioPorDia: TextView = view.findViewById(R.id.precio_por_dia)
        val diasARentar: TextView = view.findViewById(R.id.dias_a_rentar)
        val total: TextView = view.findViewById(R.id.total)
        val imagenProducto: ImageView = view.findViewById(R.id.image_producto)
        val fechaInicio: TextView = view.findViewById(R.id.fecha_inicio)
        val fechaFin: TextView = view.findViewById(R.id.fecha_fin)

    }
}

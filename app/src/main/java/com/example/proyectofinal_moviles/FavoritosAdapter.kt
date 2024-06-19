package com.example.proyectofinal_moviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoritosAdapter(
    private val listaFavoritos: MutableList<Favorito>,
    private val onDeleteClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<FavoritosAdapter.FavoritosViewHolder>() {

    class FavoritosViewHolder(view: View, onDeleteClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val favoritoImage: ImageView = view.findViewById(R.id.favorite_image)
        val favoritoName: TextView = view.findViewById(R.id.favorite_name)
        val favoritoDetails: TextView = view.findViewById(R.id.favorite_details)
        val favoritoDetails2: TextView = view.findViewById(R.id.favorite_details2)
        val favoritoPrice: TextView = view.findViewById(R.id.favorite_price)
        val deleteButton: Button = view.findViewById(R.id.favoritoEliminar)

        init {
            deleteButton.setOnClickListener {
                onDeleteClickListener(adapterPosition)
            }
        }

        fun bind(favorito: Favorito) {
            Glide.with(itemView.context)
                .load(favorito.imagen)
                .into(favoritoImage)
            favoritoName.text = favorito.nombre
            favoritoDetails.text = favorito.marca
            favoritoDetails2.text = favorito.modelo
            favoritoPrice.text = favorito.precio.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favoritos_item, parent, false)
        return FavoritosViewHolder(itemView, onDeleteClickListener)
    }

    fun eliminarFavorito(position: Int) {
        listaFavoritos.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: FavoritosViewHolder, position: Int) {
        val favorito = listaFavoritos[position]
        holder.bind(favorito)
    }

    override fun getItemCount() = listaFavoritos.size
}

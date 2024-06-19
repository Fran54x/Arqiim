package com.example.proyectofinal_moviles.crudFragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal_moviles.ClasesSinUI.ProductosClase
import com.example.proyectofinal_moviles.R

class productosAdminAdapter(
    private val listaProductos:List<ProductosClase>,
): RecyclerView.Adapter<productosAdminAdapter.AdminProductViewHolder>() {

    class AdminProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val favoritoImage: ImageView = view.findViewById(R.id.favorite_image)
        val favoritoName: TextView = view.findViewById(R.id.favorite_name)
        val favoritoDetails: TextView = view.findViewById(R.id.favorite_details)
        val favoritoDetails2: TextView = view.findViewById(R.id.favorite_details2)
        val favoritoPrice: TextView = view.findViewById(R.id.favorite_price)


        fun bind(producto: ProductosClase) {
            Glide.with(itemView.context)
                .load(producto.imagen)
                .into(favoritoImage)
            favoritoName.text = producto.nombre
            favoritoDetails.text = producto.marca
            favoritoDetails2.text = producto.modelo
            favoritoPrice.text = producto.precio.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_producto_item, parent, false)
        return AdminProductViewHolder(itemView)
    }

    override fun getItemCount() =  listaProductos.size

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.bind(producto)
    }
}
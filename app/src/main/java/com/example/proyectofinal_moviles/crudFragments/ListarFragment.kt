package com.example.proyectofinal_moviles.crudFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal_moviles.ClasesSinUI.ProductosClase
import com.example.proyectofinal_moviles.Favorito
import com.example.proyectofinal_moviles.FavoritosAdapter
import com.example.proyectofinal_moviles.ProductAdapter
import com.example.proyectofinal_moviles.R
import com.example.proyectofinal_moviles.databinding.FragmentHomeBinding
import com.example.proyectofinal_moviles.producto
import com.example.proyectofinal_moviles.ui.Favoritos.FavoritosViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ListarFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: productosAdminAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var productList: MutableList<ProductosClase>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listar, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        productList = mutableListOf()
        productAdapter = productosAdminAdapter(productList)
        recyclerView.adapter = productAdapter

        firestore = FirebaseFirestore.getInstance()
        fetchProducts()

        // Inflate the layout for this fragment
        return view
    }

    private fun fetchProducts() {
        firestore.collection("Productos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject(ProductosClase::class.java)
                    productList.add(product)
                }
                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
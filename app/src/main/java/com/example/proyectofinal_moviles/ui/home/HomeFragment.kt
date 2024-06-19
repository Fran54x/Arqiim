package com.example.proyectofinal_moviles.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal_moviles.ClasesSinUI.ProductosClase
import com.example.proyectofinal_moviles.Favorito
import com.example.proyectofinal_moviles.FavoritosAdapter
import com.example.proyectofinal_moviles.ProductAdapter
import com.example.proyectofinal_moviles.R
import com.example.proyectofinal_moviles.crudFragments.productosAdminAdapter
import com.example.proyectofinal_moviles.databinding.FragmentHomeBinding
import com.example.proyectofinal_moviles.producto
import com.example.proyectofinal_moviles.ui.Detalle.DetalleFragment
import com.example.proyectofinal_moviles.ui.Favoritos.FavoritosViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val favoritosViewModel: FavoritosViewModel by viewModels({ requireActivity() })

//    private lateinit var favoritosAdapter: FavoritosAdapter
//    private lateinit var productAdapter: productosAdminAdapter
//    private lateinit var firestore: FirebaseFirestore
//    private val listaFavoritos: MutableList<Favorito> = mutableListOf()
//    private val productList: MutableList<ProductosClase> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
//        favoritosAdapter = FavoritosAdapter(listaFavoritos) { position ->
//            eliminarFavorito(position)
//        }
//
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = productAdapter
//        }
//
//        firestore = FirebaseFirestore.getInstance()
//        fetchProducts()

        return root
    }

    private fun fetchProducts() {
//        firestore.collection("Productos")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val product = document.toObject(ProductosClase::class.java)
//                    productList.add(product)
//                }
//                productAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                // Maneja el error
//                Toast.makeText(requireContext(), "Error al cagar los datos", Toast.LENGTH_SHORT).show()
//            }
    }

    private fun onFavoritoClick(producto: ProductosClase) {
        val favorito = Favorito(
            producto.id.toInt(),
            producto.nombre,
            producto.marca,
            producto.modelo,
            producto.precio.toString(),
            producto.imagen
        )
        favoritosViewModel.agregarFavorito(favorito)

        val mensaje = "Añadido a favoritos: ${producto.nombre}"
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun eliminarFavorito(position: Int) {
        favoritosViewModel.eliminarFavorito(position)
    }

    private fun navegarADetalleFragment(producto: producto) {
        val detalleFragment = DetalleFragment().apply {
            arguments = Bundle().apply {
                putString("claveProducto", producto.id.toString())
            }
        }

        val navController = findNavController()
        navController.navigate(R.id.detalleragment, detalleFragment.arguments)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

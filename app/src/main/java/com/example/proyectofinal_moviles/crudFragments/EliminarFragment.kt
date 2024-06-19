package com.example.proyectofinal_moviles.crudFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.set
import com.example.proyectofinal_moviles.R
import com.google.firebase.firestore.FirebaseFirestore


class EliminarFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etProductId: EditText
    private lateinit var btnDeleteProduct: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eliminar, container, false)
        firestore = FirebaseFirestore.getInstance()

        etProductId = view.findViewById(R.id.edtIdProductoEliminar)
        btnDeleteProduct = view.findViewById(R.id.btnEliminarProducto)

        btnDeleteProduct.setOnClickListener {
            deleteProduct()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun deleteProduct() {
        val productId = etProductId.text.toString().trim()

        if (productId.isEmpty()) {
            Toast.makeText(context, "Por favor, proporciona el ID del producto", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Productos").document(productId).delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show()
                etProductId.setText("")
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al eliminar el producto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
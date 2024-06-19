package com.example.proyectofinal_moviles.crudFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.proyectofinal_moviles.R
import com.google.firebase.firestore.FirebaseFirestore


class ModificarFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etProductId: EditText
    private lateinit var etProductName: EditText
    private lateinit var etProductDescription: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var etProductBrand: EditText
    private lateinit var etProductModel: EditText
    private lateinit var btnUpdateProduct: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modificar, container, false)

        firestore = FirebaseFirestore.getInstance()

        etProductId = view.findViewById(R.id.etProductId)
        etProductName = view.findViewById(R.id.etProductName)
        etProductDescription = view.findViewById(R.id.etProductDescription)
        etProductPrice = view.findViewById(R.id.etProductPrice)
        etProductBrand = view.findViewById(R.id.etProductBrand)
        etProductModel = view.findViewById(R.id.etProductModel)
        btnUpdateProduct = view.findViewById(R.id.btnUpdateProduct)

        btnUpdateProduct.setOnClickListener {
            updateProduct(etProductId.text.toString().trim())

        }

        return view
    }

    private fun updateProduct(productId: String?) {
        if (productId.isNullOrEmpty()) {
            Toast.makeText(context, "ID de producto no proporcionado", Toast.LENGTH_SHORT).show()
            return
        }

        val nombre = etProductName.text.toString().trim()
        val descripcion = etProductDescription.text.toString().trim()
        val precioText = etProductPrice.text.toString().trim()
        val marca = etProductBrand.text.toString().trim()
        val modelo = etProductModel.text.toString().trim()
        val precio = precioText.toDoubleOrNull()

        if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || marca.isEmpty() || modelo.isEmpty()) {
            Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val product = hashMapOf<String, Any>(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "precio" to precio,
            "marca" to marca,
            "modelo" to modelo
        )

        firestore.collection("Productos").document(productId).update(product)
            .addOnSuccessListener {
                Toast.makeText(context, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al actualizar el producto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        etProductId.text.clear()
        etProductName.text.clear()
        etProductDescription.text.clear()
        etProductPrice.text.clear()
        etProductBrand.text.clear()
        etProductModel.text.clear()
    }
}
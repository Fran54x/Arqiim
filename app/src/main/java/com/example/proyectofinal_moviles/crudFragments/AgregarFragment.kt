package com.example.proyectofinal_moviles.crudFragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectofinal_moviles.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.UUID


class AgregarFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore

    private lateinit var edtId: EditText
    private lateinit var edtNombre: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var edtMarca: EditText
    private lateinit var edtModelo: EditText
    private lateinit var imgFoto: ImageView
    private lateinit var btnSubirImagen: Button
    private lateinit var btnAgregar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar, container, false)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        firestore = FirebaseFirestore.getInstance()

        edtId = view.findViewById(R.id.edtId);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtDescripcion = view.findViewById(R.id.edtDesc);
        edtPrecio = view.findViewById(R.id.edtPrecio);
        edtMarca = view.findViewById(R.id.edtMarca);
        edtModelo = view.findViewById(R.id.edtModelo);
        imgFoto = view.findViewById(R.id.imgFoto);
        btnSubirImagen = view.findViewById(R.id.btnSubirImagen);
        btnAgregar = view.findViewById(R.id.btnAgregar);

        btnSubirImagen.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(this. , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
//            } else {
//                selectImage()
//            }
            selectImage()
        }

        btnAgregar.setOnClickListener {
            AgregarProducto()
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    private fun AgregarProducto() {
        if (filePath != null) {
            val ref = storageReference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        saveProductToFirestore(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this.context, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this.context, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProductToFirestore(imageUrl: String) {
        val id = edtId.text.toString().trim().ifEmpty { UUID.randomUUID().toString() }
        val nombre = edtNombre.text.toString().trim()
        val descripcion = edtDescripcion.text.toString().trim()
        val precioText = edtPrecio.text.toString().trim()
        val marca = edtMarca.text.toString().trim()
        val modelo = edtModelo.text.toString().trim()
        val precio = precioText.toDoubleOrNull()

        if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || marca.isEmpty() || modelo.isEmpty()) {
            Toast.makeText(this.context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = hashMapOf(
            "id" to id,
            "nombre" to nombre,
            "descripcion" to descripcion,
            "precio" to precio,
            "marca" to marca,
            "modelo" to modelo,
            "imagen" to imageUrl
        )

        firestore.collection("Productos").document(id).set(producto)
            .addOnSuccessListener {
                edtId.setText(id)
                Toast.makeText(this.context, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this.context, "Error al agregar el producto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                imgFoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun clearFields() {
        edtId.text.clear()
        edtNombre.text.clear()
        edtDescripcion.text.clear()
        edtPrecio.text.clear()
        edtMarca.text.clear()
        edtModelo.text.clear()
        imgFoto.setImageResource(0)
        filePath = null
    }

}
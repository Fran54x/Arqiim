package com.example.proyectofinal_moviles

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PruebasActivity : AppCompatActivity() {

    private lateinit var foto: ImageView
    private lateinit var btnCamara: ImageButton
    private lateinit var btnGalleria: ImageButton
    private lateinit var btnEliminar: ImageButton
    private lateinit var mfirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private var imageUri: Uri? = null
    private lateinit var idd: String

    private val storagePath = "perfil/"
    private val photo = "photo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruebas)

        progressDialog = ProgressDialog(this)
        val id = intent.getStringExtra("id_pet")
        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        idd = id.toString()

        foto = findViewById(R.id.imgPruebaImagen)
        btnCamara = findViewById(R.id.imgPruebaTomarFoto)
        btnGalleria = findViewById(R.id.imgPruebaGalleria)
        btnEliminar = findViewById(R.id.imgPruebaEliminarFoto)

        btnCamara.setOnClickListener { tomarFoto() }
        btnGalleria.setOnClickListener { seleccionarFoto() }
        btnEliminar.setOnClickListener { eliminarFoto() }
    }

    private fun seleccionarFoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun tomarFoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        responseLauncher.launch(intent)
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val extras = activityResult.data?.extras
            val imgBitmap = extras?.get("data") as Bitmap?
            if (imgBitmap != null) {
                foto.setImageBitmap(imgBitmap)
                imageUri = getImageUri(imgBitmap)
                imageUri?.let { subirFoto(it) }
            } else {
                Toast.makeText(this, "Error al tomar la foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Proceso cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            imageUri = activityResult.data?.data
            if (imageUri != null) {
                foto.setImageURI(imageUri)
                subirFoto(imageUri!!)
            } else {
                Toast.makeText(this, "Error al seleccionar la foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Proceso cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun subirFoto(imageUri: Uri) {
        progressDialog.setMessage("Actualizando foto")
        progressDialog.show()

        val ruteStoragePhoto = "$storagePath$photo/ejemplo"
        val reference = storageReference.child(ruteStoragePhoto)

        reference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    foto.setImageURI(uri)
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseStorage", "Error al subir la imagen: ${exception.message}")
                progressDialog.dismiss()
            }
    }

    private fun eliminarFoto() {
        // Implementar la lógica para eliminar la foto
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
}

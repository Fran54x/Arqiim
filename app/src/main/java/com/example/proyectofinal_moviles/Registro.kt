package com.example.proyectofinal_moviles

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private val CHANNEL_ID = "Canal_notificacion"
    private val textTitle = "Registro con Éxito"
    private val textContent = "Este es el texto informativo de la notificacion"
    private val notificationId = 100
    companion object {
        val usuariosRegistrados = mutableListOf<Usuario>()
        var ultimoId = 0
    }

    private lateinit var nombre: EditText
    private lateinit var password: EditText
    private lateinit var correo: EditText
    private lateinit var telefono: EditText
    private lateinit var checkBoxTerminos: CheckBox
    private lateinit var usuarios: Usuario
    private var coleccion = "Usuarios"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        nombre = findViewById(R.id.edtUs)
        password = findViewById(R.id.edtPassword)
        correo = findViewById(R.id.edtCorreo)
        telefono = findViewById(R.id.edtTelefono)
        checkBoxTerminos = findViewById(R.id.checkTerminos)
        usuarios = Usuario()

        //Crear canal de notificacion y definir importancia
        createNotificationChannel()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        supportActionBar?.hide()

        val textViewTerms = findViewById<TextView>(R.id.txtTermino)

        textViewTerms.setOnClickListener {
            val intent = Intent(this, terminos::class.java)
            startActivity(intent)
        }

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            registrarUsuario()

        }
    }

    private fun regresar(){
        val intent = Intent(this, login::class.java)
        startActivity(intent)
    }

    private fun registrarUsuario() {
        //Isntancia a la base de datos
        val bdAgenda = FirebaseFirestore.getInstance()

        if (
            password.text.isNotBlank() && password.text.isNotEmpty() &&
            nombre.text.isNotBlank() && nombre.text.isNotEmpty() &&
            correo.text.isNotBlank() && correo.text.isNotEmpty() &&
            telefono.text.isNotBlank() && telefono.text.isNotEmpty()
        ) {

            //Obtener valores del formulario
            val name = nombre.text.toString()
            val email = correo.text.toString()
            val contra = password.text.toString()
            val phone = telefono.text.toString().toLong()
            val terminos = if (checkBoxTerminos.isChecked) "Aceptado" else ""


            //Identificador del documento(concatenacion de datos
            val idDocumento = email
            //Agregar documento a la coleccion, con sus campos
            //Si quieres que firebase cree la serie de identificadores .add
            bdAgenda.collection(coleccion).document(idDocumento).set(
                hashMapOf(
                    "nombre" to name,
                    "correo" to email,
                    "password" to contra,
                    "telefono" to phone,
                    "condicion" to terminos
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Se registro con exito", Toast.LENGTH_SHORT).show()
                regresar()
                finish()
//                limpiarFormulario()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT)
                    .show()
            }
        }else{
            Toast.makeText(this, "coloque todas las cajas", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun notificacionBasica() {
        //Definir caracteristicas de la notificacion
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("Bienvenido al grupo ARQUIIM"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //Mostrar notificacion
        with(NotificationManagerCompat.from(this)){
            //Notificacion is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
        Toast.makeText(applicationContext, "Notificacion", Toast.LENGTH_SHORT).show()
    }//NotificacionBasica

    private fun createNotificationChannel() {
        val name = "My Channel"
        val descriptionText = "Descripcion del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val nofificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nofificationManager.createNotificationChannel(channel)
    }//createNotificacion Chanel
}


package com.example.proyectofinal_moviles

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.SQLException

class login : AppCompatActivity() {

    private lateinit var admin: ControladorBD
//    private lateinit var fila: Cursor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        admin = ControladorBD(this, "arquiim.db", null,1)
        val textViewRegistrate = findViewById<TextView>(R.id.Registrate)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIngresar)
        val edtUs = findViewById<EditText>(R.id.edtUs2)
        val edtPassword = findViewById<EditText>(R.id.edtPassword2)

        textViewRegistrate.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener {
            val correo = edtUs.text.toString()
            val password = edtPassword.text.toString()
            if(correo == "administrador" && password == "123"){
                Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Crud::class.java)
                startActivity(intent)
            }else{
                iniciarSesion(correo, password)
            }

        }
    }

    private fun iniciarSesion(correo: String, password: String) {
        val bd = FirebaseFirestore.getInstance()
        if(correo.isNotBlank() && correo.isNotEmpty() &&
            password.isNotBlank() && password.isNotEmpty()){

            bd.collection("Usuarios").document(correo).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val pass = document.getString("password")
                        if(password == pass){
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            guardarCredenciales(correo, password)
                            //val intent = Intent(this, PruebasActivity::class.java)
                            val intent = Intent(this, Productos::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "$correo y $pass", Toast.LENGTH_SHORT).show()
                            //finish()
                        }else{
                            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }


        }else{
            Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show()
        }
    }
    private fun guardarCredenciales(correo: String, pass:String){
        val sharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("correo", correo)
        editor.putString("pass",pass)
        editor.apply()
    }
}


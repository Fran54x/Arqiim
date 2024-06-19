package com.example.proyectofinal_moviles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Crud : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tvBienvenido: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)

        tvBienvenido = findViewById(R.id.tvBienvenido)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)


        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("username", "Desconocido")
        tvBienvenido.text = "Bienvenido, $nombreUsuario"

        // Configurar el ViewPager con el adapter
        viewPager.adapter = ViewPagerAdapter(this)

        // Configurar el TabLayout con el ViewPager
        val tabTitles = listOf("Agrega", "Modifica", "Elimina", "Lista");
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()



    }
}
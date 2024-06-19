package com.example.proyectofinal_moviles

data class producto(val id:Int,
                    val nombre: String,
                    val marca: String,
                    val modelo: String,
                    val precio: String,
                    val descrip: String,
                    val imagen: Int,
                    var diasARentar: Int = 1,
                    var precioTotal: String = precio,
                    val fechaInicio: String? = null,
                    val fechaFin: String? = null
)

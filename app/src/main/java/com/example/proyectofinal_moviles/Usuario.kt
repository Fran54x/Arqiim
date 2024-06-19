package com.example.proyectofinal_moviles

data class Usuario(
    var nombre: String ?= null,
    var correo: String ?= null,
    var password: String ?= null,
    var telefono: Int ?= null,
    var condicion: Int ?= null
)

package com.example.proyectofinal_moviles

data class Usuarios(
    val id :Int,
    val usuario: String,
    var correo: String,
    val telefono: String,
    val password: String,
    var imageUriString: String? = null
) {

}

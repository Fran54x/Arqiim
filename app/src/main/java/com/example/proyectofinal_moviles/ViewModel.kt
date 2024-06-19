package com.example.proyectofinal_moviles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _carrito = MutableLiveData<List<producto>>(emptyList())
    val carrito: LiveData<List<producto>> = _carrito

    fun agregarAlCarrito(producto: producto) {
        val listaActual = _carrito.value?.toMutableList() ?: mutableListOf()
        listaActual.add(producto)
        _carrito.value = listaActual
    }

    fun eliminarDelCarrito(producto: producto) {
        val currentCart = carrito.value ?: emptyList()
        val updatedCart = currentCart.filter { it.id != producto.id }
        _carrito.value = updatedCart
    }
    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }
    private val _historial = MutableLiveData<List<producto>>()
    val historial: LiveData<List<producto>> = _historial

    fun agregarAHistorial(productos: List<producto>, fechaInicio: String, fechaFin: String) {
        val productosConTotal = productos.map { producto ->
            val precio = producto.precio.replace("$", "").toDoubleOrNull() ?: 0.0
            val totalCalculado = precio * producto.diasARentar

            producto.copy(precioTotal = String.format("%.2f", totalCalculado), fechaInicio = fechaInicio, fechaFin = fechaFin)
        }
        _historial.value = _historial.value.orEmpty() + productosConTotal
    }


}



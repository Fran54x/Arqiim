package com.example.proyectofinal_moviles.ui.Favoritos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyectofinal_moviles.Favorito

class FavoritosViewModel : ViewModel() {
    private val _listaFavoritos = MutableLiveData<MutableList<Favorito>>()
    val listaFavoritos: LiveData<MutableList<Favorito>> get() = _listaFavoritos

    init {
        _listaFavoritos.value = mutableListOf()
    }

    // Función para añadir un favorito a la lista
    fun agregarFavorito(favorito: Favorito) {
        _listaFavoritos.value?.add(favorito)
        _listaFavoritos.postValue(_listaFavoritos.value)
        Log.d("FavoritosViewModel", "Favorito añadido: $favorito")
    }

    fun eliminarFavorito(position: Int) {
        _listaFavoritos.value?.removeAt(position)
        _listaFavoritos.postValue(_listaFavoritos.value)
    }

}

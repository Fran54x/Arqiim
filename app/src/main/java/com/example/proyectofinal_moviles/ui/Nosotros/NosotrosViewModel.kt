package com.example.proyectofinal_moviles.ui.Nosotros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NosotrosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Visitanos :D"
    }
    val text: LiveData<String> = _text
}
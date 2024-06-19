package com.example.proyectofinal_moviles.ui.Ayuda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal_moviles.databinding.AyudaBinding
import com.example.proyectofinal_moviles.ui.Ayuda.AyudaViewModel

class AyudaFragment : Fragment() {

    private var _binding: AyudaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val AyudaViewModel =
            ViewModelProvider(this).get(AyudaViewModel::class.java)

        _binding = AyudaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
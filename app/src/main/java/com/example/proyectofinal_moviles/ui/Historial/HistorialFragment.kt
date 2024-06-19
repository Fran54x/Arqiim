package com.example.proyectofinal_moviles.ui.Historial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal_moviles.HistorialAdapter
import com.example.proyectofinal_moviles.databinding.ActivityHistorialBinding
import com.example.proyectofinal_moviles.SharedViewModel

class HistorialFragment : Fragment() {

    private var _binding: ActivityHistorialBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = ActivityHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHistorial.layoutManager = LinearLayoutManager(context)
        sharedViewModel.historial.observe(viewLifecycleOwner) { historial ->
            binding.recyclerViewHistorial.adapter = HistorialAdapter(historial)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

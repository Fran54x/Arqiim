package com.example.proyectofinal_moviles.ui.Nosotros
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal_moviles.databinding.ActivityNosotrosBinding
import com.example.proyectofinal_moviles.WebViewActivity

class NosotrosFragment : Fragment() {

    private var _binding: ActivityNosotrosBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding no puede ser accedido.")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val NosotrosViewModel = ViewModelProvider(this)[NosotrosViewModel::class.java]

        _binding = ActivityNosotrosBinding.inflate(inflater, container, false)

        binding.textNosotros.text = NosotrosViewModel.text.value

        binding.btnWeb.setOnClickListener {
            Web()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Web() {
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        startActivity(intent)
    }
}

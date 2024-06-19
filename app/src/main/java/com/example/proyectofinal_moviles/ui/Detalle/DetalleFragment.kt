package com.example.proyectofinal_moviles.ui.Detalle

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal_moviles.R
import com.example.proyectofinal_moviles.SharedViewModel
import com.example.proyectofinal_moviles.databinding.ActivityDetalleBinding
import com.example.proyectofinal_moviles.producto

class DetalleFragment : Fragment() {
    private var _binding: ActivityDetalleBinding? = null
    private val binding get() = _binding!!
    private var precioPorDia: Double = 0.0
    private var diasARentar: Int = 1
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        sharedPref = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        _binding = ActivityDetalleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val productoId = arguments?.getString("claveProducto")?.toIntOrNull()
        productoId?.let {
            val productoEncontrado = obtenerProducto(it)
            productoEncontrado?.let { producto ->
                binding.productImage.setImageResource(producto.imagen)
                binding.txtproductName.text = producto.nombre
                binding.txtproductMarca.text = producto.marca
                binding.txtproductModelo.text = producto.modelo
                binding.txtproductDescri.text = producto.descrip

                val precioSinSimbolo = producto.precio.replace("$", "").trim()
                precioPorDia = precioSinSimbolo.toDoubleOrNull() ?: 0.0

                binding.txtproductPrice.text = getString(R.string.total_format, precioPorDia)

                actualizarPrecioTotal()
            }
        }
        productoId?.let {
            diasARentar = sharedPref.getInt("DiasARentar_$it", 1)
        }
        binding.txtQuantity.text = diasARentar.toString()

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val starCountTextView = view.findViewById<TextView>(R.id.tvStarCount)
        val productId = obtenerIdDelProducto()
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        val savedRating = sharedPref?.getFloat("RatingForProduct_$productId", -1f) ?: -1f

        if (savedRating != -1f) {
            ratingBar.rating = savedRating
            starCountTextView.text = savedRating.toString()
        }

        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(context, "Calificaste con $rating estrellas", Toast.LENGTH_SHORT).show()
            starCountTextView.text = rating.toString()

            sharedPref?.edit()?.apply {
                putFloat("RatingForProduct_$productId", rating)
                apply()
            }

        }
        sharedPref = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val productoId = arguments?.getString("claveProducto")?.toIntOrNull()


        diasARentar = sharedPref.getInt("DiasARentar_${productoId ?: 0}", 1)
        binding.txtQuantity.text = diasARentar.toString()

        binding.btnIncrease.setOnClickListener {
            diasARentar++
            binding.txtQuantity.text = diasARentar.toString()
            sharedPref.edit().putInt("DiasARentar_${productoId ?: 0}", diasARentar).apply()
            actualizarPrecioTotal()
        }

        binding.btnDecrease.setOnClickListener {
            if (diasARentar > 1) {
                diasARentar--
                binding.txtQuantity.text = diasARentar.toString()
                sharedPref.edit().putInt("DiasARentar_${productoId ?: 0}", diasARentar).apply()
                actualizarPrecioTotal()
            }
        }
        binding.btnRentar.setOnClickListener {
            agregarAlCarrito()
        }



    var diasARentar = sharedPref?.getInt("DiasARentar_$productId", 1) ?: 1
    binding.txtQuantity.text = diasARentar.toString()



}
    private fun actualizarPrecioTotal() {
        val precioTotal = precioPorDia * diasARentar
        binding.txtproductPrice.text = getString(R.string.total_format, precioTotal)
    }

    private fun obtenerIdDelProducto(): String {
        return "id_del_producto_actual"
    }

    private fun agregarAlCarrito() {
        val productoId = arguments?.getString("claveProducto")?.toIntOrNull()
        productoId?.let { id ->
            val productoActual = obtenerProducto(id)
            productoActual?.let { producto ->
                producto.diasARentar = diasARentar
                producto.precioTotal = calcularPrecioTotal(precioPorDia, diasARentar).toString()
                sharedViewModel.agregarAlCarrito(producto)
                resetDetalleView()
                Toast.makeText(context, "Artículo ${producto.nombre} agregado al carrito", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calcularPrecioTotal(precioPorDia: Double, diasARentar: Int): Double {
        return precioPorDia * diasARentar
    }

    private fun resetDetalleView() {

        diasARentar = 1
        binding.txtQuantity.text = diasARentar.toString()
        actualizarPrecioTotal()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun obtenerProducto(id_producto: Int): producto? {
        val productos = listOf(
            producto(1, "Caterpillar D8T Bulldozer", "Marca: Caterpillar", "Modelo: D8T", "$1000","Descripcion asdfasdfasdf", R.drawable.prod1,1)
//            producto(R.drawable.prod1, "Caterpillar D8T Bulldozer", "Marca: Caterpillar", "Modelo: D8T", "$1000", "1", "Un potente bulldozer para trabajos de construcción pesada y minería."),
//            producto(R.drawable.prod1, "Caterpillar D8T Bulldozer", "Marca: Caterpillar", "Modelo: D8T", "$1000", 1, "Un potente bulldozer para trabajos de construcción pesada y minería."),
//            producto(R.drawable.prod2, "Liebherr LTM 11200-9.1 Grúa Móvil", "Marca: Liebherr", "Modelo: LTM 11200-9.1", "$2000", 2, "Una de las grúas móviles más grandes y potentes, utilizada en construcción y montaje de estructuras."),
//            producto(R.drawable.prod3, "Komatsu PC8000-6 Excavadora", "Marca: Komatsu", "Modelo: PC8000-6", "$3000", 3, "Excavadora de minería de alto rendimiento para operaciones de gran escala."),
//            producto(R.drawable.prod4, "Volvo A60H Dumper Articulado", "Marca: Volvo", "Modelo: A60H", "$500", 4, "Camión dumper para transporte de grandes cantidades de material en construcción y minería."),
//            producto(R.drawable.prod5, "John Deere 9620RX Tractor Agrícola", "Marca: John Deere", "Modelo: 9620RX", "$5000", 5, "Tractor de alta potencia para trabajos agrícolas extensivos."),
//            producto(R.drawable.prod6, "Mack Granite Camión de Basura", "Marca: Mack Trucks", "Modelo: Granite", "$3424", 6, "Camión robusto diseñado para la recolección y transporte de residuos."),
//            producto(R.drawable.prod7, "Hitachi ZX870 Excavadora de Orugas", "Marca: Hitachi", "Modelo: ZX870", "$5934", 7, "Excavadora de gran tamaño para construcción y proyectos de ingeniería civil."),
//            producto(R.drawable.prod8, "MAN TGX 41.640 Camión de Transporte Pesado", "Marca: MAN", "Modelo: TGX 41.640", "$900", 8, "Camión para transporte de cargas pesadas y sobredimensionadas."),
//            producto(R.drawable.prod9, "Terex Demag AC 500-2 Grúa Todo Terreno", "Marca: Terex Demag", "Modelo: AC 500-2", "$508", 9, "Grúa móvil con capacidades de elevación excepcionales, adecuada para terrenos difíciles."),
//            producto(R.drawable.prod10, "Bobcat T870 Cargador Compacto de Orugas", "Marca: Bobcat", "Modelo: T870", "$400", 10, "Cargador compacto de alto rendimiento para construcción y paisajismo."),
//            producto(R.drawable.prod3, "Doosan DA40 Camión Articulado", "Marca: Doosan", "Modelo: DA40", "$6509", 11, "Camión articulado para operaciones de minería y construcción de carreteras."),
//            producto(R.drawable.prod1, "JCB 220X Excavadora de Orugas", "Marca: JCB", "Modelo: 220X", "$837", 12, "Excavadora robusta y eficiente para proyectos de construcción de mediana a gran escala."),
//            producto(R.drawable.prod13, "Case IH Magnum 380 CVX Tractor", "Marca: Case IH", "Modelo: Magnum 380 CVX", "$6721", 13, "Tractor de gran potencia para tareas agrícolas exigentes."),
//            producto(R.drawable.prod4, "Kenworth W990 Camión de Largo Recorrido", "Marca: Kenworth", "Modelo: W990", "$1092", 14, "Camión diseñado para transporte de larga distancia, combinando comodidad y rendimiento."),
//            producto(R.drawable.prod15, "Bell B45E Camión Dumper Articulado", "Marca: Bell Equipment", "Modelo: B45E", "$922", 15, "Camión dumper para el transporte eficiente de materiales en minas y grandes proyectos de construcción.")
        )
        return productos.find { it.id == id_producto }
    }

}
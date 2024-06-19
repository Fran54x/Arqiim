package com.example.proyectofinal_moviles.ui.Miscompras
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal_moviles.CancelarCompra
import com.example.proyectofinal_moviles.ExitoRenta
import com.example.proyectofinal_moviles.R
import com.example.proyectofinal_moviles.SharedViewModel
import com.example.proyectofinal_moviles.databinding.ActivityComprasBinding
import com.example.proyectofinal_moviles.producto
import java.util.Calendar
import java.util.Locale

class MiscomprasFragment : Fragment() {
    private var _binding: ActivityComprasBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var cartAdapter: CartAdapter
    private var total: Double = 0.0
    private var fechaInicio: String? = null
    private var fechaFin: String? = null

    //Notificacion
    private val CHANNEL_ID = "Canal_notificacion"
    private val textTitle = "Registro con Éxito"
    private val textContent = "Este es el texto informativo de la notificacion"
    private val notificationId = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = ActivityComprasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeCart()
        updateTotal()

        val btnRentarAhora = binding.root.findViewById<Button>(R.id.btnRentarAhora)

        btnRentarAhora.setOnClickListener {

            showDatePickerDialog("inicio") { selectedDateInicio ->
                fechaInicio = selectedDateInicio

                showDatePickerDialog("fin") { selectedDateFin ->
                    fechaFin = selectedDateFin

                    val carrito = sharedViewModel.carrito.value ?: emptyList()
                    if (carrito.isNotEmpty() && fechaInicio != null && fechaFin != null) {
                        val articulosRentados = carrito.joinToString { it.nombre }
                        val mensaje = "Artículos rentados: $articulosRentados desde $fechaInicio hasta $fechaFin"
                        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()

                        //Notificacion de botones
                        notificationBoton()

                        sharedViewModel.agregarAHistorial(carrito, fechaInicio!!, fechaFin!!)
                        sharedViewModel.limpiarCarrito()


                        fechaInicio = null
                        fechaFin = null

                } else {
                        Toast.makeText(context, "El carrito está vacío o las fechas no son válidas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun showDatePickerDialog(tipo: String, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.setTitle(if (tipo == "inicio") "Selecciona la fecha de inicio" else "Selecciona la fecha de fin")
        datePickerDialog.show()
    }


    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(mutableListOf()) { producto ->
            eliminarDelCarrito(producto)
        }
        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = cartAdapter
    }

    private fun eliminarDelCarrito(producto: producto) {
        sharedViewModel.eliminarDelCarrito(producto)
        Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
        updateTotal()
    }

    private fun observeCart() {
        sharedViewModel.carrito.observe(viewLifecycleOwner) { updatedCart ->
            cartAdapter.setData(updatedCart)
            updateTotal()
        }
    }

    private fun updateTotal() {
        var total = 0.0
        for (producto in sharedViewModel.carrito.value ?: emptyList()) {
            val precio = producto.precio.replace("$", "").toDoubleOrNull() ?: 0.0
            val dias = producto.diasARentar
            total += precio * dias
        }

        val totalFormatted = "$${String.format("%.2f", total)}"

        binding.tvTotal.text = "Total: $totalFormatted"
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->

            val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
            Toast.makeText(context, "Fecha seleccionada: $selectedDate", Toast.LENGTH_SHORT).show()

        }, 2023, 12, 4)

        datePickerDialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun notificationBoton() {
        val accionSi = Intent(requireActivity(), ExitoRenta::class.java).apply{
            putExtra("accion",1)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val accionNo = Intent(requireActivity(), CancelarCompra::class.java).apply{
            putExtra("accion",2)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntentSi: PendingIntent = PendingIntent.getActivity(requireContext(),0,accionSi,
            PendingIntent.FLAG_IMMUTABLE)

        val pendingIntentNo: PendingIntent = PendingIntent.getActivity(requireContext(),0,accionNo,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(textTitle)
            .setContentText("Notificacion con Boton")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.baseline_notifications_24, getString(R.string.aceptar),
                pendingIntentSi)
            .addAction(R.drawable.baseline_notifications_24, getString(R.string.cancelar),
                pendingIntentNo)
            .setAutoCancel(true)

        //Mostrar notificacion
        with(NotificationManagerCompat.from(requireContext())){
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = "My Channel"
        val descriptionText = "Descripcion del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val nofificationManager: NotificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nofificationManager.createNotificationChannel(channel)
    }//createNotificacion Chanel


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



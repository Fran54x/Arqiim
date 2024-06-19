package com.example.proyectofinal_moviles

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {


    //Variables para la ubicacion del usuario
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var minimumDistance = 30
    private val PERMISSION_LOCATION = 999
    private var latInicial: Double = 0.0
    private var longInicial: Double = 0.0



    private val callback = OnMapReadyCallback { googleMap ->

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }//onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest.create()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.smallestDisplacement = minimumDistance.toFloat()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                // Obtener la ubicacion del usuario
                val location = locationResult.lastLocation

                // actualizar las variables de latitud y longitud inicial
                latInicial = location!!.latitude
                longInicial = location!!.longitude

                Log.e(
                    "APP 06",
                    locationResult.lastLocation?.latitude.toString() + "," +
                            locationResult.lastLocation?.longitude
                )

            }
        } //LocationCallback
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mMap.isMyLocationEnabled = true
            startLocationUpdates() //Obtenemos la ubicacion del usuario
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
                PERMISSION_LOCATION
            )
        }

        //// Get the JSON file from the raw folder
//        val inputStream: InputStream = resources.openRawResource(R.raw.ruta)
//        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = StringBuilder()
//
//        //Leemos el contenido del archivo JSON y convertido a una cadena JSON
//        var line: String? = reader.readLine()
//        while(line != null){
//            jsonString.append(line)
//            line = reader.readLine()
//        }

        //Parsear el contenido json
//        val json = JSONObject(jsonString.toString())
//        val puntosArray = json.getJSONArray("puntos")

        // Lista para almacenar las ubicaciones de destino
        val destinos = mutableListOf<LatLng>()

        //Iterar sobre los puntos y agregar marcadores al mapa
//        for (i in 0 until puntosArray.length()){
//            val punto = puntosArray.getJSONObject(i)
//            val nombre = punto.getString("nombre")
//            val latitudDestino = punto.getDouble("latitud")
//            val longitudDestino = punto.getDouble("longitud")

//            val ubicacion = LatLng(latitudDestino, longitudDestino )

//            mMap.addMarker(MarkerOptions().position(ubicacion).title(nombre))

            // Agregar la ubicación a la lista de destinos
//            destinos.add(ubicacion)
//        }

        for (destino in destinos) {
            val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$latInicial,$longInicial&destination=${destino.latitude},${destino.longitude}&key=AIzaSyD5pSDAbjMDaY1a1ale6RTbELmiAPxvj94"
            requestDirectionsApi(url)
        }


        // Add a marker in Sydney and move the camera
        val gdl = LatLng(20.62202804305603, -103.30574224962452)
        mMap.addMarker(
            MarkerOptions().position(gdl).title("Empresa")
                .snippet("Constructora Coreda")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gdl, 15f))
        mMap.setOnMapClickListener(this)

    }//onMapReady

    // Función para hacer la solicitud a la API de Direcciones
    private fun requestDirectionsApi(url: String) {
        val requestQueue = Volley.newRequestQueue(requireContext())

        // Crea una solicitud HTTP GET con la URL proporcionada
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                val routesArray = jsonResponse.getJSONArray("routes")
                if (routesArray.length() > 0) {
                    //  procesar la ruta
                    val routeObject = routesArray.getJSONObject(0)
                    val polylineObject = routeObject.getJSONObject("overview_polyline")
                    val points = polylineObject.getString("points")

                    // Decodificar los puntos de la polilinea
                    val decodedPath = PolyUtil.decode(points)

                    // Crear una lista de LatLng a partir de los puntos decodificados
                    val latLngList = ArrayList<LatLng>()
                    for (point in decodedPath) {
                        latLngList.add(LatLng(point.latitude, point.longitude))
                    }

                    // Agrega la polilinea al mapa
                    mMap.addPolyline(PolylineOptions().addAll(latLngList).color(Color.BLUE))
                } else {
                    Log.e("Error", "El array de rutas está vacío")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            })

        // Agrega la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest)
    }

    //Actualizar la localizacion del usuario
    private fun startLocationUpdates() {
        try {mFusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
        } catch (e: SecurityException) {

        }
    }//startLocationUpdates

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }//stopLocationUpdates

    //Permisos Para la ubicacion del usuario
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray ) {
        super.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults )

        if (requestCode == PERMISSION_LOCATION) {
            if (permissions[0].equals( Manifest.permission_group.LOCATION, ignoreCase = true )
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }//onStart

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }//onPause

    override fun onMapClick(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 13f))
        mMap.addMarker(
            MarkerOptions()
                .title("Marca personal")
                .snippet("Mi sitio marcado")
                .draggable(true)
                .icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.iconomundo)
                )
                .position(latLng)
        )
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$latInicial,$longInicial&destination=${latLng.latitude},${latLng.longitude}&key=AIzaSyD5pSDAbjMDaY1a1ale6RTbELmiAPxvj94"

        // Solicitar la ruta a la API de direcciones
        requestDirectionsApi(url)
    }
}
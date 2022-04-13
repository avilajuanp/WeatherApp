package com.gncelectronic.climaapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gncelectronic.climaapp.api.API_KEY
import com.gncelectronic.climaapp.api.DARK_SKY_URL
import com.gncelectronic.climaapp.api.JSONParser
import com.gncelectronic.climaapp.modelo.ClimaActual
import com.gncelectronic.climaapp.modelo.Dia
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    lateinit var userLocation: LatLng

    val jsonParser = JSONParser()

    lateinit var dias: ArrayList<Dia>

    companion object {
        val CLIMA_SEMANAL = "CLIMA_SEMANAL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocalization()
        getClima()
        btnDailyWeather.setOnClickListener { getClimaSemanal() }
        btnHorWeather.setOnClickListener { getClimaPorHora() }
    }


    /**
     * Obtener los datos del clima actual
     */
    private fun getClima() {
        val latitud = userLocation.latitude
        val longitud = userLocation.longitude
        val lang = "es"
        val unit = "auto"

        val url = "$DARK_SKY_URL/$API_KEY/$latitud,$longitud?lang=$lang&units=$unit"

        //cola de mensajes de solicitud
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                val responseJSON = JSONObject(response)
                val climaActual = jsonParser.getClimaActualFromJson(responseJSON)
                dias = jsonParser.getClimaSemanalFromJson(responseJSON)

                llenarClimaActualUI(climaActual, jsonParser.getTimeZone(responseJSON))

            },
            Response.ErrorListener { error ->
                mostrarMensajeDeError()
                Log.d(TAG, "Error en la llamada. %s".format(error.toString()))
            }
        )
        queue.add(stringRequest)
    }

    /**
     * Obtener la posicion GPS o localizacion
     */
    private fun MainActivity.getLocalization() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("Not yet implemented")
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("Not yet implemented")
            }

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2,
                2f,
                locationListener
            )
            val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)

        }
    }

    private fun llenarClimaActualUI(climaActual: ClimaActual, timeZone: String) {
        localizationTextView.text = timeZone
        summaryTextView.text = climaActual.summary
        imageIcon.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                climaActual.getIconResource(),
                null
            )
        )
        temperaturaTextView.text = "${climaActual.temperature.toInt()} °C"
        val precipProb = (climaActual.precipProb * 100).toInt()
        probPrecipTextView.text = "${precipProb}%"
    }


    private fun mostrarMensajeDeError() {
        val snackBar = Snackbar.make(mainLayout, "Error conexión", Snackbar.LENGTH_INDEFINITE)
            .setAction("Reitentar?") {
                getClima()
            }
    }


    fun getClimaSemanal() {
        val intent = Intent(this, ClimaSemanal::class.java)
        intent.putParcelableArrayListExtra(CLIMA_SEMANAL, dias)
        startActivity(intent)
    }


    fun getClimaPorHora() {}


}

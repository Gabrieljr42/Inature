package com.example.inature3.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.inature3.controller.TrilhaController
import com.example.inature3.controller.UserController
import com.example.inature3.model.Caminho
import com.example.inature3.model.Localizacao
import com.example.inature3.model.Trilha
import com.example.inature3.utility.UserDataHolder
import java.util.Date
import kotlin.math.abs
import kotlin.math.ceil


class GPSEvents private constructor() {

    constructor(cont: Context?):this(){
        contexto = cont
    }

    companion object {
        var locationManager: LocationManager? = null
        var lat:Double? = null
        var lng: Double? = null
        var oldLat:Double? = null
        var oldLng:Double? = null
        var oldTime:Long? = 0
        var mtAndados: Double? = null
        val userDataHolder = UserDataHolder.getInstance()
        val email = userDataHolder.email
        lateinit var trilha:Trilha

        //atributos da nossa aplicação
        private var caminhoAtivo: Caminho? = null
        private var latTxt: TextView? = null
        private var lngText: TextView? = null
        var contexto: Context? = null
        var gpsInstance: GPSEvents? = null
        fun getInstance(cont: Context?, c: Caminho?, lat: TextView?, lng: TextView?): GPSEvents? {
            println("get instanceeeeeeeee")
            contexto = cont
            latTxt = lat
            lngText = lng
            caminhoAtivo = c
            if (gpsInstance == null) {
                gpsInstance = GPSEvents(cont)
            }
            return gpsInstance
        }
    }

    fun atualizaGPS(trilha1:Trilha) {
        trilha = trilha1
        configuraGPS()
    }

    private fun configuraGPS() {
        locationManager = contexto!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //sera que o usuario perm. o acesso a local.
        if (ActivityCompat.checkSelfPermission(
                contexto!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                contexto!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(contexto, "Sem permissão do acesso a gps", Toast.LENGTH_SHORT).show()
            return
        }


        //primeira tentativa via sensor GPS
        var provider: String? = null
        if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER
            println("net")
        }
        if (provider == null && locationManager!!.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            provider = LocationManager.PASSIVE_PROVIDER
            println("pass")
        }
        if (provider == null && locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER
            println("gps")
        }

        /*if(locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)){
            provider = LocationManager.FUSED_PROVIDER;
            System.out.println("fused");
        }*/locationManager!!.requestLocationUpdates(
            provider!!, 1, 1f
        ) { location ->
            println("Update")

            if (location != null) {
                lat = location.latitude
                lng = location.longitude
                latTxt!!.text = String.format("%.6f", lat) + ""
                lngText!!.text = String.format("%.6f", lng) + ""

                mtAndados = 0.0
                if(oldLng != lng && oldLat != lat){
                    if(oldLng == null){
                        oldLng = lng
                        oldLat = lat
                    }else{
                        mtAndados = abs(lat!! - oldLat!!) + (lng!! - oldLng!!)
                    }
                }
                mtAndados = mtAndados!! * 100000
                val mtAndadosInt: Int = ceil(mtAndados!!).toInt()

                val currentTime = System.currentTimeMillis()
                val elapsedTimeInMillis = currentTime - oldTime!!
                val elapsedTimeInSeconds = elapsedTimeInMillis / 1000
                val tempoAdicional = elapsedTimeInSeconds.toString()

                oldTime = currentTime



                val useCon = UserController(contexto!!)
                useCon.incrementaPontos(email!!, abs(mtAndadosInt!!))

                val trilhaCon = TrilhaController(contexto!!)
                trilhaCon.atualizaUsuarioTrilha(email, trilha.id, tempoAdicional, mtAndadosInt.toDouble())

                val novoPonto = Localizacao(lat!!, lng!!, Date(), 0.0)
                caminhoAtivo?.let { novoPonto.id=it.id }
            }
        }
    }


    fun getLat(): Double? {
        return lat
    }

    fun getLng(): Double? {
        return lng
    }

    fun setLat(lat: Double) {
        GPSEvents.lat = lat
    }

    fun setLng(lng: Double) {
        GPSEvents.lng = lng
    }
}
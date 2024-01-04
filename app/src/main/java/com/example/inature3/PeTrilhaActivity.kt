package com.example.inature3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import com.codebyashish.googledirectionapi.AbstractRouting
import com.codebyashish.googledirectionapi.ErrorHandling
import com.codebyashish.googledirectionapi.RouteDrawing
import com.codebyashish.googledirectionapi.RouteInfoModel
import com.codebyashish.googledirectionapi.RouteListener
import com.example.inature3.controller.TrilhaController
import com.example.inature3.databinding.ActivityPeTrilhaBinding
import com.example.inature3.model.Caminho
import com.example.inature3.model.Trilha
import com.example.inature3.tools.GPSEvents
import com.example.inature3.utility.UserDataHolder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.Date


class PeTrilhaActivity : AppCompatActivity(), OnClickListener, OnMapReadyCallback, RouteListener  {
    lateinit var binding:ActivityPeTrilhaBinding
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    lateinit var qrResultLauncher: ActivityResultLauncher<Intent>
    lateinit var destinationLocation:LatLng
    lateinit var userLocation:LatLng
    lateinit var trilha:Trilha

    private var mGoogleMap:GoogleMap? = null
    private var idTrilha:Long = -1
    private var gpsManager: GPSEvents? = null
    private var ativo:Caminho = Caminho(1, dia=Date())
    private var handler: Handler? = null
    private var horaInicial: Date? = null
    private var atualziarElementos:Boolean = true
    private var userLat:Double = 0.0
    private var userLng:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPeTrilhaBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_pe_trilha)
        setContentView(binding.root)

        lerDadosIntent()

        startMaps()

        registraEventos()

        iniciaCamposActivity()
    }
    fun startMaps(){
        val mapFragment =  supportFragmentManager.findFragmentById(binding.imagemMapaParque.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }
    fun iniciaCamposActivity(){
        val sf:SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        binding.txtDataTrilha.text = sf.format(Date())

        //inicia GPS
        horaInicial = Date()
        executaHandleElementosDinamicos();

        requestGPS();
    }

    fun lerDadosIntent(){
        idTrilha = intent.extras.let { it!!.getLong("id",-1) }

        val trilhaCon = TrilhaController(baseContext)

        trilha = trilhaCon.lerTrilha(idTrilha)!!
        binding.txtNomeTrilha.text = trilha!!.nomeTrilha

    }

    fun registraEventos(){
        binding.btnVoltar.setOnClickListener(this)

        binding.btnSalvarEncerrar.setOnClickListener(this)

        binding.btnQrCode.setOnClickListener(this)

        qrResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                var bundle:Bundle? = result.data?.extras
                //println(bundle!!.getString("qrcode"))
                /*if (bundle != null) {
                    bundle.getString("qrcode")
                }*/
                //faz algo com o valor retornado, deve ser acrescido a conquista do usuário em banco -> server
                val intent = Intent()
                if (bundle != null) {
                    //intent.putExtra("qrcode", bundle.getString("qrcode"))

                    val intent = Intent(baseContext, ShowFragmentActivity::class.java)
                    intent.putExtra("fragment","premiacao")
                    intent.putExtra("qrcode", bundle.getString("qrcode"))

                    startActivity(intent)
                }
            })
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.btnVoltar.id -> finish()
            binding.btnSalvarEncerrar.id -> salvar()
            binding.btnQrCode.id -> leituraQr()
        }
    }

    fun salvar(){
        val trilhaCon = TrilhaController(baseContext)
        val userDataHolder = UserDataHolder.getInstance()

        trilhaCon.finalizarTrilha(userDataHolder.email!!, trilha.id )

        finish()
    }

    fun leituraQr(){
        val tratarQr:Intent = Intent(baseContext, LeitorQrActivity::class.java)
        qrResultLauncher.launch(tratarQr)
    }

    private fun requestGPS() {
        if (gpsManager == null) {
            gpsManager = GPSEvents.getInstance(this, ativo, binding.txtLat, binding.txtLng)
        }
        gpsManager!!.atualizaGPS(trilha)
    }

    private fun executaHandleElementosDinamicos() {
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                atualizaHora()
            }
        }
        Thread {
            // TODO Auto-generated method stub
            while (atualziarElementos) {
                try {
                    Thread.sleep(1000)
                    (handler as Handler).post(Runnable { // TODO Auto-generated method stub
                        atualizaHora()
                    })
                } catch (e: Exception) {
                    // TODO: handle exception
                }
            }
        }.start()
    }

    private fun atualizaHora() {
        val agora = Date()
        var milliDif: Long = agora.time - (horaInicial?.getTime() ?: agora.time)
        val horas = milliDif / 3600000
        milliDif = milliDif % 3600000
        val minutos = milliDif / 60000
        milliDif = milliDif % 60000
        val segundos = milliDif / 1000
        binding.txtTempoTrilha.setText(
            (if (horas < 10) "0" else "") + horas + ":"
                    + (if (minutos < 10) "0" else "") + minutos + ":"
                    + (if (segundos < 10) "0" else "") + segundos
        )
    }
    fun obtemPontosDaRota(start:LatLng , end:LatLng ){
        //Git repo :https://github.com/dangiashish/Google-Direction-Api
        val routeDrawing = RouteDrawing.Builder()
            .context(this)
            .travelMode(AbstractRouting.TravelMode.WALKING)
            .withListener(this).alternativeRoutes(true)
            .waypoints(start, end)
            .build()
        routeDrawing.execute();
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(googleMap: GoogleMap) {
      mGoogleMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        //Colocando minha localização
        mGoogleMap!!.setMyLocationEnabled(true)

        pegandoMinhaLocalizacaoNoMapa()
    }
    fun pegandoMinhaLocalizacaoNoMapa(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        var task:Task<Location> =  fusedLocationProviderClient.getLastLocation()
        task.addOnSuccessListener {
            userLat = it.latitude
            userLng = it.longitude

            //obtendo os pontos da rota
            userLocation = LatLng(userLat, userLng)
            destinationLocation = LatLng(trilha.lat, trilha.lng)
            obtemPontosDaRota(userLocation, destinationLocation)

            val latLng:LatLng = LatLng(userLat, userLng)
            val cam:CameraPosition = CameraPosition.builder().target(latLng).zoom(20F).build()

            mGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cam))

        }
    }

    override fun onRouteFailure(e: ErrorHandling?) {

    }

    override fun onRouteStart() {

    }

    override fun onRouteSuccess(list: ArrayList<RouteInfoModel>?, indexing: Int) {
        //Desenhando os pontos no mapa
        val polylineOptions = PolylineOptions()
        val polylines = ArrayList<Polyline>()
        for (i in 0 until list!!.size) {
            if (i == indexing) {
                Log.e("TAG", "onRoutingSuccess: routeIndexing$indexing")
                polylineOptions.color(Color.RED) //Definindo a cor da rota
                polylineOptions.width(12f)
                polylineOptions.addAll(list.get(indexing).getPoints())
                polylineOptions.startCap(RoundCap())
                polylineOptions.endCap(RoundCap())
                val polyline: Polyline = mGoogleMap!!.addPolyline(polylineOptions)
                polylines.add(polyline)
            }
        }
    }

    override fun onRouteCancelled() {

    }
}
package com.example.inature3

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.inature3.controller.TrilhaController
import com.example.inature3.databinding.ActivityUserBinding
import com.example.inature3.model.Trilha


class UserActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityUserBinding
    lateinit var nomeUser:String
    lateinit var emailUser:String

    companion object {
        lateinit var qrResultLauncher: ActivityResultLauncher<Intent>
        lateinit var peNaTrilhaLauncher: ActivityResultLauncher<Intent>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_user)
        setContentView(binding.root)

        iniciaValores()

        iniciaFragmentoUser()

        registraEventos()

        preparaDB()
    }
    private fun preparaDB() {

        val prefs = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val trilhasInserted = prefs.getBoolean("trilhasInserted", false)

        if (!trilhasInserted) {
            val trilhaCon = TrilhaController(this)
            inserirExemploTrilhas(trilhaCon)

            // Mark trilhas as inserted
            prefs.edit().putBoolean("trilhasInserted", true).apply()
        }
    }
    private fun inserirExemploTrilhas(trilhaCon: TrilhaController) {

        val trilhas = listOf(
            Trilha(1, "Minha casa", "15 min", 1.0, 2.0, -20.510674, -43.707443 ),
            Trilha(2, "IFMG", "15 min", 3.0, 2.5, -20.513507, -43.712699 ),
        )

        for (trilha in trilhas) {
            trilhaCon.inserirTrilha(trilha)
        }

    }
    fun registraEventos(){

        ActivityCompat.requestPermissions(
            this, arrayOf<String>(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            ), 1
        )


        trocaViewSelecao(binding.imagemAccount)
        binding.imagemTrofeu.setOnClickListener(this)
        binding.imagemQrcode.setOnClickListener(this)
        binding.imagemAccount.setOnClickListener(this)
        binding.imagemPath.setOnClickListener(this)

        qrResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                var bundle:Bundle? = result.data?.extras
                /*if (bundle != null) {
                    bundle.getString("qrcode")
                }*/
                replaceFragment(MostrarConquistaQRFragment(), R.id.fragment_user)
            })

        peNaTrilhaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                var bundle:Bundle? = result.data?.extras
                if (bundle != null && bundle.getString("qrcode") != null) {
                    println(bundle.getString("qrcode"))
                    replaceFragment(MostrarConquistaQRFragment(), R.id.fragment_user)
                }
                //replaceFragment(PeTrilhaFragment(), R.id.fragment_user)
            })
    }

    fun iniciaFragmentoUser(){

        val bundle = Bundle()
        bundle.putString("nome", nomeUser)
        bundle.putString("email", emailUser)

        val primeiroFragment:UserFragment = UserFragment()
        primeiroFragment.arguments = bundle

        replaceFragment(primeiroFragment, R.id.fragment_user)
    }

    fun iniciaValores(){
        val valores: Bundle? = intent.extras

        if(valores != null){
            nomeUser = valores.getString("nome","")
            emailUser = valores.getString("email","")
        }else{
            Toast.makeText(baseContext,"usuário não cadastrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment, idFrame:Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(idFrame, fragment)
        transaction.commit()
    }

    private fun trocaViewSelecao(v:View){
        val botoesSup:List<ImageView> = listOf(binding.imagemAccount, binding.imagemPath, binding.imagemTrofeu, binding.imagemQrcode)
        val viewSelec:List<View> = listOf(binding.viewAccount, binding.viewCaminho, binding.viewTrofeu, binding.viewQrcode)

        for(i in (0..3)){
            if(botoesSup[i].id == v.id){
                viewSelec[i].visibility=View.VISIBLE
            }else{
                viewSelec[i].visibility=View.INVISIBLE
            }
        }
    }

    override fun onClick(v: View) {

        when(v.id){
            binding.imagemAccount.id -> iniciaFragmentoUser()
            binding.imagemQrcode.id -> leitorQrCode()
            binding.imagemPath.id -> iniciaTracker()
            binding.imagemTrofeu.id -> mostreConquistas()
        }

        trocaViewSelecao(v)
    }

    fun leitorQrCode(){
        //val leitorQr:Intent = Intent(baseContext, ScannedBarcodeActivity::class.java)
        val tratarQr:Intent = Intent(baseContext, LeitorQrActivity::class.java)
        qrResultLauncher.launch(tratarQr)
    }

    fun iniciaTracker(){
        val fragment:TrilhaFragment = TrilhaFragment()
        //peNaTrilhaLauncher
        replaceFragment(fragment, R.id.fragment_user)
    }

    fun mostreConquistas(){
        replaceFragment(PremiacaoFragment(), R.id.fragment_user)
    }
}
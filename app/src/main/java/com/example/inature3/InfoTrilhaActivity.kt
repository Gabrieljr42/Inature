package com.example.inature3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inature3.data.MockTrilhasUser
import com.example.inature3.databinding.ActivityInfoTrilhaBinding
import com.example.inature3.listas.adapter.InfoGeralAdapter
import com.example.inature3.model.InformacaoGenerica
import com.example.inature3.model.Trilha

class InfoTrilhaActivity : AppCompatActivity(), OnClickListener {
    lateinit var binding: ActivityInfoTrilhaBinding

    lateinit var nome:String //deveria ser o id, alterar depois quando tivermos BD
    private val adapterInfoGeral: InfoGeralAdapter = InfoGeralAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoTrilhaBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_info_trilha)
        setContentView(binding.root)

        carregarInformacoes()

        registrarEventos()
    }

    fun carregarInformacoes(){
//        intent.extras.let {
//            nome = it!!.getString("id","")
//        }
//
//        binding.txtNomeTitulo.text = nome
//
//        val mock:MockTrilhasUser = MockTrilhasUser()
//
//        val trilhas:List<Trilha> = mock.trilhasDisponiveis.filter { it.nomeTrilha==nome }
//
//        if(!trilhas.isEmpty()){
//            val infos:List<InformacaoGenerica> = trilhas[0].infos
//
//            binding.infoTrilhaList.layoutManager = LinearLayoutManager(baseContext)
//            binding.infoTrilhaList.adapter = adapterInfoGeral
//
//            adapterInfoGeral.updateInfoGeral(infos)
//        }
    }

    fun registrarEventos(){
        binding.btnVoltar.setOnClickListener(this)

        binding.btnIniciar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.btnVoltar.id -> finish()
            binding.btnIniciar.id -> iniciaTrilha()
        }

    }

    fun iniciaTrilha(){

    }

}
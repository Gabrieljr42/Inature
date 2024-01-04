package com.example.inature3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inature3.controller.TrilhaController
import com.example.inature3.data.MockQrsUser
import com.example.inature3.databinding.FragmentUserBinding
import com.example.inature3.listas.adapter.QrCompletaAdapter
import com.example.inature3.listas.adapter.TrilhaCompletaAdapter
import com.example.inature3.model.QrCode
import com.example.inature3.model.Trilha
import android.content.Context
import com.example.inature3.model.UserTrilha
import kotlin.math.abs

class UserFragment : Fragment(), OnClickListener {
    // TODO: Rename and change types of parameters
    private var nomeUser: String? = null
    private var emailUser: String? = null

    lateinit var binding:FragmentUserBinding
    lateinit var trilhas:List<Trilha>
    lateinit var qrs:List<QrCode>
    private val adapterTrilha: TrilhaCompletaAdapter = TrilhaCompletaAdapter()
    private val adapterQrs: QrCompletaAdapter = QrCompletaAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentUserBinding.inflate(layoutInflater)

        arguments?.let {
            nomeUser = it.getString("nome")
            emailUser = it.getString("email")
        }


        binding.textUserName.text = nomeUser

        carregaTrilhas()
        carregaNivel()
        registrarEventos()
    }


    fun carregaNivel(){
        val userTrilha = TrilhaController(requireContext())
        var  mtFalta = 0.0

        var mtTrilhas = userTrilha.calcularDistanciaTotalPorUsuario(emailUser!!)
        mtTrilhas = abs(mtTrilhas)
        binding.textDistancia.setText("${(mtTrilhas)} MT \n percorridos")
        if(mtTrilhas < 500){
            mtFalta = mtTrilhas - 500
            binding.textFaltaNivel.setText("Faltam ${abs(mtFalta)} mt para o 2\n próximo nível")
            binding.textLevel.setText("Nível 1")
        }else{
            binding.textLevel.setText("Nível 2")
            binding.textFaltaNivel.setText("Faltam 15000 MT para o\\npróximo nível")
        }


    }
    fun registrarEventos(){
        binding.textTrilhas.setOnClickListener(this)

        binding.textQrCodeColetado.setOnClickListener(this)
    }



    private fun carregaTrilhas(){
        val trilhaCon = TrilhaController(requireContext())
        val trilhasList = trilhaCon.obterTrilhasPorUsuario(emailUser!!);
        binding.listaTrilhasQrCodes.layoutManager = LinearLayoutManager(context)
        binding.listaTrilhasQrCodes.adapter = adapterTrilha

        adapterTrilha.updateTrilhas(trilhasList)
//
//        context?.let { binding.textTrilhas.setTextColor(it.getColor(R.color.textselecao)) }
//        context?.let { binding.textQrCodeColetado.setTextColor(it.getColor(R.color.black)) }
//
        binding.txtQuantidadeListados.text = "Você percorreu \n${trilhaCon.usuarioRealizouQuantasTrilhas(
            emailUser!!
        )}\ntrilhas até o momento"

    }

    private fun carregaQrCodes(){
//        val mock:MockQrsUser = MockQrsUser()
//        qrs = mock.qrs
//
//        binding.listaTrilhasQrCodes.layoutManager = LinearLayoutManager(context)
//        binding.listaTrilhasQrCodes.adapter = adapterQrs
//
//        adapterQrs.updateTrilhas(qrs)
//
//        context?.let { binding.textTrilhas.setTextColor(it.getColor(R.color.black)) }
//        context?.let { binding.textQrCodeColetado.setTextColor(it.getColor(R.color.textselecao)) }

        binding.txtQuantidadeListados.text = "Você coletou\n0\nQrCodes até o momento"
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString("nome", param1)
                }
            }
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.textTrilhas.id-> carregaTrilhas()

            binding.textQrCodeColetado.id -> carregaQrCodes()
        }
    }
}
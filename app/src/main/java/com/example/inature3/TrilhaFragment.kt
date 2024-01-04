package com.example.inature3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inature3.controller.TrilhaController
import com.example.inature3.data.MockTrilhasUser
import com.example.inature3.databinding.FragmentTrilhaBinding
import com.example.inature3.listas.adapter.TrilhaDisponivelAdapter
import com.example.inature3.tools.GPSEvents
import com.example.inature3.utility.UserDataHolder


class TrilhaFragment : Fragment(), OnClickListener {

    lateinit var binding:FragmentTrilhaBinding
    private val adapterTrilhaDisponivel: TrilhaDisponivelAdapter = TrilhaDisponivelAdapter()

    // TODO: Rename and change types of parameters
    //private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }*/
        binding = FragmentTrilhaBinding.inflate(layoutInflater)

        carregaTrilha()

        registraEventos()
    }

    fun registraEventos(){
        binding.textTrilhas.setOnClickListener(this)

        binding.textQrCodeColetado.setOnClickListener(this)
    }

    private fun carregaTrilha(completa:Boolean = true){


        var trilhas = TrilhaController(requireContext()).obterTodasTrilhas();

        if (!completa) {
            trilhas = trilhas.filter { trilha ->
                val trilhaCon = TrilhaController(requireContext())
                val userDataHolder = UserDataHolder.getInstance()
                val userTrilha = trilhaCon.obterUsuarioTrilha(userDataHolder.email!!, trilha.id)


                userTrilha?.nota != -1
            }
        }

        binding.listaTrilhas.layoutManager = LinearLayoutManager(context)
        binding.listaTrilhas.adapter = adapterTrilhaDisponivel

        adapterTrilhaDisponivel.updateTrilhas(trilhas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
        TrilhaFragment().apply {
            arguments = Bundle().apply {
                //putString(ARG_PARAM1, param1)
               //putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            binding.textTrilhas.id -> {
                carregaTrilha(true)
                context?.let { binding.textTrilhas.setTextColor(it.getColor(R.color.textselecao)) }
                context?.let { binding.textQrCodeColetado.setTextColor(it.getColor(R.color.black)) }
            }
            binding.textQrCodeColetado.id -> {
                carregaTrilha(false)
                context?.let { binding.textTrilhas.setTextColor(it.getColor(R.color.black)) }
                context?.let { binding.textQrCodeColetado.setTextColor(it.getColor(R.color.textselecao)) }

            }
        }
    }

}
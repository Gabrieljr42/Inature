package com.example.inature3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inature3.controller.UserController
import com.example.inature3.databinding.FragmentPremiacaoBinding
import com.example.inature3.listas.adapter.RankAdapter


lateinit var binding: FragmentPremiacaoBinding
private val adapterRank: RankAdapter = RankAdapter()


class PremiacaoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPremiacaoBinding.inflate(layoutInflater)

        carregaRank()
    }

    private fun carregaRank(){
        val rank = UserController(requireContext()).obtemUsuarios()

        binding.listaTrilhas.layoutManager = LinearLayoutManager(context)
        binding.listaTrilhas.adapter = adapterRank

        adapterRank.updateRank(rank!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PremiacaoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
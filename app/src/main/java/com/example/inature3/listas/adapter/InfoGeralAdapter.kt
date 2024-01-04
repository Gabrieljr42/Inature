package com.example.inature3.listas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemInfoTrilhaBinding
import com.example.inature3.databinding.ItemTrilhaCompletaBinding
import com.example.inature3.listas.holder.InfoGeralViewHolder
import com.example.inature3.listas.holder.TrilhaViewHolder
import com.example.inature3.model.InformacaoGenerica
import com.example.inature3.model.Trilha

class InfoGeralAdapter: RecyclerView.Adapter<InfoGeralViewHolder>(){
    private var trilhaList: List<InformacaoGenerica> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoGeralViewHolder {
        val item = ItemInfoTrilhaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoGeralViewHolder(item)
    }

    override fun getItemCount(): Int {
        return trilhaList.count()
    }

    override fun onBindViewHolder(holder: InfoGeralViewHolder, position: Int) {
        holder.bind(trilhaList[position])
    }

    fun updateInfoGeral(list: List<InformacaoGenerica>) {
        trilhaList = list
        notifyDataSetChanged()
    }
}
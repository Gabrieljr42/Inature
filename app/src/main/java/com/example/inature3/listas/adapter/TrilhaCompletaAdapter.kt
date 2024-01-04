package com.example.inature3.listas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemTrilhaCompletaBinding
import com.example.inature3.listas.holder.TrilhaViewHolder
import com.example.inature3.model.Trilha

class TrilhaCompletaAdapter: RecyclerView.Adapter<TrilhaViewHolder>(){
    private var trilhaList: List<Trilha> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrilhaViewHolder {
        val item = ItemTrilhaCompletaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrilhaViewHolder(item)
    }

    override fun getItemCount(): Int {
        return trilhaList.count()
    }

    override fun onBindViewHolder(holder: TrilhaViewHolder, position: Int) {
        holder.bind(trilhaList[position])
    }

    fun updateTrilhas(list: List<Trilha>) {
        trilhaList = list
        notifyDataSetChanged()
    }
}
package com.example.inature3.listas.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.UserActivity.Companion.peNaTrilhaLauncher
import com.example.inature3.databinding.ItemTrilhaDisponivelBinding
import com.example.inature3.listas.holder.TrilhaDisponivelViewHolder
import com.example.inature3.model.Trilha

class TrilhaDisponivelAdapter() : RecyclerView.Adapter<TrilhaDisponivelViewHolder>(){
    private var trilhaList: List<Trilha> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrilhaDisponivelViewHolder {
        val item = ItemTrilhaDisponivelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrilhaDisponivelViewHolder(item)
    }

    override fun getItemCount(): Int {
        return trilhaList.count()
    }

    override fun onBindViewHolder(holder: TrilhaDisponivelViewHolder, position: Int) {
        holder.bind(trilhaList[position])
    }

    fun updateTrilhas(list: List<Trilha>) {
        trilhaList = list
        notifyDataSetChanged()
    }



}
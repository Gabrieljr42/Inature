package com.example.inature3.listas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemQrCodeBinding
import com.example.inature3.listas.holder.QrViewHolder
import com.example.inature3.listas.holder.TrilhaViewHolder
import com.example.inature3.model.QrCode
import com.example.inature3.model.Trilha

class QrCompletaAdapter: RecyclerView.Adapter<QrViewHolder>(){
    private var trilhaList: List<QrCode> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrViewHolder {
        val item = ItemQrCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QrViewHolder(item)
    }

    override fun getItemCount(): Int {
        return trilhaList.count()
    }

    override fun onBindViewHolder(holder: QrViewHolder, position: Int) {
        holder.bind(trilhaList[position])
    }

    fun updateTrilhas(list: List<QrCode>) {
        trilhaList = list
        notifyDataSetChanged()
    }
}
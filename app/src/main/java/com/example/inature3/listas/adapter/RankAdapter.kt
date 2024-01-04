package com.example.inature3.listas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.R
import com.example.inature3.databinding.ItemRankingBinding
import com.example.inature3.listas.holder.RankViewHolder
import com.example.inature3.model.UserRank

class RankAdapter: RecyclerView.Adapter<RankViewHolder>(){
    private var rankList: List<UserRank> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val item = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //item.imagemTrofeu.setImageDrawable(parent.context.getDrawable(R.drawable.trofeu1))
        return RankViewHolder(item)
    }

    override fun getItemCount(): Int {
        return rankList.count()
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(rankList[position], position)
    }

    fun updateRank(list: List<UserRank>) {
        rankList = list
        notifyDataSetChanged()
    }
}
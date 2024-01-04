package com.example.inature3.listas.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemInfoTrilhaBinding
import com.example.inature3.model.InformacaoGenerica
import java.time.format.DateTimeFormatter
import java.util.Locale

class InfoGeralViewHolder(private val item:ItemInfoTrilhaBinding) : RecyclerView.ViewHolder(item.root) {

    fun bind(info: InformacaoGenerica) {
        // Atribui valores
        item.txtTitutloInfo.text = info.titulo
        item.txtDescricao.text = info.descricao
    }
}
package com.example.inature3.listas.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.R
import com.example.inature3.databinding.ItemQrCodeBinding
import com.example.inature3.databinding.ItemRankingBinding
import com.example.inature3.model.QrCode
import com.example.inature3.model.UserRank
import java.time.format.DateTimeFormatter
import java.util.Locale

class RankViewHolder(private val item:ItemRankingBinding) : RecyclerView.ViewHolder(item.root) {


    val avatares:List<Int> = listOf(
        R.drawable.avant1, R.drawable.avant10,R.drawable.avant11,R.drawable.avant12, R.drawable.avant13,
        R.drawable.avant14, R.drawable.avant15, R.drawable.avant16, R.drawable.avant2,
        R.drawable.avant3, R.drawable.avant4, R.drawable.avant5, R.drawable.avant6, R.drawable.avant7,
        R.drawable.avant8, R.drawable.avant9
    )

    fun bind(rank: UserRank, pos:Int) {
        // Atribui valores
        item.textPontuacao.text = rank.pontos.toString()
        item.txtNome.text = rank.nome

        item.imagemAvatarUser.setImageDrawable(item.root.context.getDrawable(avatares[pos]))

        if(pos <= 2){
            item.imagemTrofeu.visibility = View.VISIBLE

            when(pos){
                0 -> item.imagemTrofeu.setImageDrawable(item.root.context.getDrawable(R.drawable.trofeu1))
                1 -> item.imagemTrofeu.setImageDrawable(item.root.context.getDrawable(R.drawable.trofeu2))
                2 -> item.imagemTrofeu.setImageDrawable(item.root.context.getDrawable(R.drawable.trofeu3))
            }
        }

        if(pos % 2 == 0){
            item.viewPai.setBackgroundColor(item.root.context.getColor(R.color.verdeescuro))
        }
    }
}
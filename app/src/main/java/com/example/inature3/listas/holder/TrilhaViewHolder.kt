package com.example.inature3.listas.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemTrilhaCompletaBinding
import com.example.inature3.model.Trilha
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.ceil

class TrilhaViewHolder(private val item:ItemTrilhaCompletaBinding) : RecyclerView.ViewHolder(item.root) {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ENGLISH)
    fun bind(trilha: Trilha) {

        // Atribui valores
        item.textDataItem.text = "Hoje"
        item.textNomeTrilhaItem.text = "Trilha: ${trilha.nomeTrilha}"
        item.labelTempoGasto.text = "Tempo gasto: ${ceil(trilha.tempoEstimado.toDouble())} minutos" //
        item.txtDistancia.text = "Metros gastos: ${abs(trilha.distancia)}"
    }
}
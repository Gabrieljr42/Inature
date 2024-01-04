package com.example.inature3.listas.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.databinding.ItemQrCodeBinding
import com.example.inature3.model.QrCode
import java.time.format.DateTimeFormatter
import java.util.Locale

class QrViewHolder(private val item:ItemQrCodeBinding) : RecyclerView.ViewHolder(item.root) {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ENGLISH)
    fun bind(qrCode: QrCode) {

        // Atribui valores
        item.textDataItem.text = formatter.format(qrCode.dia)
        item.textNomeTrilhaItem.text = "Trilha: ${qrCode.nomeTrilha}"
        item.txtQrcodeNome.text = qrCode.nomeQr
        item.txtNivel.text = qrCode.nivel
    }
}
package com.example.inature3.listas.holder

import android.app.ProgressDialog
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.inature3.InfoTrilhaActivity
import com.example.inature3.PeTrilhaActivity
import com.example.inature3.controller.TrilhaController
import com.example.inature3.databinding.ItemTrilhaDisponivelBinding
import com.example.inature3.model.Trilha
import com.example.inature3.utility.UserDataHolder

class TrilhaDisponivelViewHolder(private val item:ItemTrilhaDisponivelBinding) : RecyclerView.ViewHolder(item.root) {

    fun bind(trilha: Trilha) {


        item.txtNomeTrilha.text = trilha.nomeTrilha
        item.txtTempoEstimado.text = "Tempo estimado: ${trilha.tempoEstimado}"
        item.txtDistancia.text = "Km: ${trilha.distancia}"

        val trilhaCon = TrilhaController(item.root.context)
        val userDataHolder = UserDataHolder.getInstance()
        val userTrilha = trilhaCon.obterUsuarioTrilha(userDataHolder.email!!, trilha.id)

        if (userTrilha?.nota != -1) {
            item.imagemMapIcon.visibility = View.VISIBLE
        } else {
            item.imagemMapIcon.visibility = View.INVISIBLE
        }

        var dialog = ProgressDialog(item.root.context)
        item.viewPai.setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
                dialog.setMessage("Carregando...")
                dialog.show()
                val handler = Handler()


                val delayMillis = 3000L

                handler.postDelayed({
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }, delayMillis)
                val infoTrilha:Intent = Intent(item.root.context, PeTrilhaActivity::class.java)

                infoTrilha.putExtra("id",trilha.id)


                item.root.context.startActivity(infoTrilha)
            }
        })
    }

}
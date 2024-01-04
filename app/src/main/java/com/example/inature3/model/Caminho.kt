package com.example.inature3.model

import java.util.Date
import java.util.Vector




data class Caminho(
    var id: Long = 0L,
    var nome: String? = null,
    var dia: Date? = null,
    var status:Int = 0){

    var pontos: MutableList<Localizacao>? = null

    init {
        pontos = mutableListOf()
    }

    fun addPonto(novoPonto: Localizacao) {
        pontos!!.add(novoPonto)
    }

}
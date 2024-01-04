package com.example.inature3.controller

import android.content.Context
import com.example.inature3.data.TrilhaDBHelper
import com.example.inature3.data.UserTrilhaDBHelper
import com.example.inature3.model.Trilha
import com.example.inature3.model.UserTrilha


class TrilhaController(context: Context) {

    private val trilhaDBHelper = TrilhaDBHelper(context)
    private val userTrilhaDBHelper = UserTrilhaDBHelper(context)

    fun inserirTrilha(nomeTrilha: String, tempoEstimado: String, dificuldade: Double, distancia: Double, lat : Double, lng: Double) {
        val trilha = Trilha(0, nomeTrilha, tempoEstimado, dificuldade, distancia, lat, lng)
        trilhaDBHelper.inserirTrilha(trilha)
    }
    fun inserirTrilha(trilha: Trilha) {
        trilhaDBHelper.inserirTrilha(trilha)
    }
    fun obterTodasTrilhas(): List<Trilha> {
        return trilhaDBHelper.obterTodasTrilhas()
    }
    fun lerTrilha(idTrilha: Long): Trilha? {
        return trilhaDBHelper.lerTrilha(idTrilha)
    }

    fun inserirUsuarioTrilha(emailUsuario: String, idTrilha: Long, tempoGasto: String, nota: Int, mtAndados:Double) {
        userTrilhaDBHelper.inserirUsuarioTrilha(emailUsuario, idTrilha, tempoGasto, nota, mtAndados)
    }
    fun atualizaUsuarioTrilha(emailUsuario: String, idTrilha: Long, tempoGasto: String,  mtAndados:Double){
        userTrilhaDBHelper.atualizarUsuarioTrilha(emailUsuario, idTrilha, tempoGasto, mtAndados)
    }
    fun obterUsuarioTrilha(emailUsuario: String, idTrilha: Long): UserTrilha? {
        return userTrilhaDBHelper.obterUsuarioTrilha(emailUsuario, idTrilha)
    }
    fun usuarioRealizouTrilha(emailUsuario: String, idTrilha: Long): Boolean {
        return userTrilhaDBHelper.obterUsuarioTrilha(emailUsuario, idTrilha) != null
    }
    fun usuarioRealizouQuantasTrilhas(emailUsuario: String):Int{
        return userTrilhaDBHelper.quantasTrilhasUsuarioAndou(emailUsuario)
    }
    fun obterTrilhasPorUsuario(emailUsuario: String): List<Trilha>{
        return userTrilhaDBHelper.obterTrilhas(emailUsuario)
    }
    fun finalizarTrilha(emailUsuario: String, idTrilha: Long){
         userTrilhaDBHelper.adicionarNotaTrilha(emailUsuario, idTrilha,  -1)
    }
    fun calcularDistanciaTotalPorUsuario(emailUsuario: String) : Double{
        return userTrilhaDBHelper.calcularDistanciaTotal(emailUsuario)
    }
}

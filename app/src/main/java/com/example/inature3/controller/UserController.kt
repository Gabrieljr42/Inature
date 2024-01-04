package com.example.inature3.controller

import android.content.Context
import com.example.inature3.data.UserDBHelper
import com.example.inature3.model.UserRank

class UserController(contexto: Context) {

    private val userDbHelper = UserDBHelper(contexto)

    fun registraUsuario(email: String, nome: String, senha: String) {
        userDbHelper.inserirUsuario(email, nome, senha)
    }
    fun obtemUsuarios(): List<UserRank>?  {
        return userDbHelper.obterTodosUsuarios()
    }
    fun lerUsuario(email: String): String? {
        return userDbHelper.lerUsuario(email)
    }
    fun incrementaPontos(email: String, pontos:Int) {
         userDbHelper.incrementarPontos(email, pontos)
    }

}
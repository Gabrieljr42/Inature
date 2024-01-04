package com.example.inature3.data

import android.content.Context
import android.content.SharedPreferences
import java.util.prefs.Preferences

class SharedTool(var contexto:Context) {

    lateinit var shared:SharedPreferences

    init {
        shared = contexto.getSharedPreferences("inature3", Context.MODE_PRIVATE)
    }

    //método temporário para teste do app v. 0.0.1 (gambs para ser removida :D )
    //Estamos utilizando o DB agora, então essa classe ficou depreciada :)

    fun registraUser(email:String, nome:String, senha:String){
        val escrita = shared.edit()
        escrita.putString(email, "$nome|$senha")
        escrita.commit()
    }

    fun lerUser(email: String): String? {
        return shared.getString(email,"")
    }

}
package com.example.inature3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.inature3.controller.UserController
import com.example.inature3.data.SharedTool
import com.example.inature3.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity(){

    lateinit var binding:ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_cadastro)
        setContentView(binding.root)

        registraEventos()
    }

    fun registraEventos(){
        binding.botaoCriar.setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
                armazenaUser()
            }
        })
    }

    fun armazenaUser(){
        val nome:String = binding.textNome.text.toString()
        val email:String = binding.textEmail.text.toString()
        val senha1:String = binding.textSenha1.text.toString()
        val senha2:String = binding.textSenha2.text.toString()
        val cidade:String = binding.textCidade.text.toString()

        if((nome == null || nome.isEmpty()) || (email == null || email.isEmpty()) || (senha1 == null || senha1.isEmpty())
            || (senha2 == null || senha2.isEmpty()) || (cidade == null || cidade.isEmpty())){
            Toast.makeText(baseContext, "Os campos devem ser preenchidos", Toast.LENGTH_SHORT).show()
        }else{
            if(senha1 != senha2){
                Toast.makeText(baseContext, "senhas não conferem", Toast.LENGTH_SHORT).show()
            }else{
                /*
                Utilizando o DB no lugar do Shared
                val shared:SharedTool = SharedTool(baseContext)
                shared.registraUser(email, nome, senha1)
                */
                val userController = UserController(this)
                userController.registraUsuario(email, nome, senha1)
                finish()
            }
        }
    }
}
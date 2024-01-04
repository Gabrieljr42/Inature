package com.example.inature3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.inature3.controller.UserController
import com.example.inature3.databinding.ActivityMainBinding
import com.example.inature3.utility.UserDataHolder

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        registrarEventos()
    }



    fun registrarEventos(){
        binding.botaoLogin.setOnClickListener(this)

        binding.textCadastro.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when(v.id){
            binding.botaoLogin.id -> inicaLogin()

            binding.textCadastro.id -> iniciaCadastro()
        }

    }

    private fun inicaLogin(){

        //val shared:SharedTool = SharedTool(baseContext)
        val userController = UserController(baseContext)

        val emailUser = binding.textEmail.text.toString()

        if(!emailUser.isEmpty()) {

            //val infosUser = shared.lerUser(emailUser)!!.split("|")
            val infosUser = userController.lerUsuario(emailUser)?.split("|")

            if(!infosUser.isNullOrEmpty() && infosUser.size == 2) {
                val nomeUser: String = infosUser[0]
                val senhaUser: String = infosUser[1]
                val senhaDigitada: String = binding.textPassword.text.toString()

                if (!senhaDigitada.isEmpty() && senhaDigitada == senhaUser) {
                    val trocaLogin: Intent = Intent(baseContext, UserActivity::class.java)
                    trocaLogin.putExtra("nome", nomeUser)
                    trocaLogin.putExtra("email", emailUser)
                    UserDataHolder.getInstance().nome = nomeUser
                    UserDataHolder.getInstance().email = emailUser
                    startActivity(trocaLogin)
                    finish()
                } else {
                    Toast.makeText(baseContext, "senha ou email incorretos", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(baseContext, "Usuário não cadastrado", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun iniciaCadastro(){
        val trocaCadastro:Intent = Intent(baseContext, CadastroActivity::class.java)
        startActivity(trocaCadastro)
    }

}
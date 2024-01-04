package com.example.inature3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.inature3.databinding.ActivityShowFragmentBinding

class ShowFragmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowFragmentBinding
    lateinit var nomeFragment:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowFragmentBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_show_fragment)
        setContentView(binding.root)

        lerDados()

        setFragment()

    }

    private fun lerDados(){
        nomeFragment = intent.extras!!.getString("fragment", null)
    }

    private fun setFragment(){
        when(nomeFragment){
            "premiacao" -> {val fragment:MostrarConquistaQRFragment = MostrarConquistaQRFragment()
                replaceFragment(fragment, R.id.fragment_user)}
        }
    }

    fun replaceFragment(fragment: Fragment, idFrame:Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(idFrame, fragment)
        transaction.commit()
    }

}
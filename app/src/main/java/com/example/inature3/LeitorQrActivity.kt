package com.example.inature3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inature3.databinding.ActivityLeitorQrBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class LeitorQrActivity : AppCompatActivity(){

    lateinit var binding:ActivityLeitorQrBinding

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(ScanContract()) {
            result: ScanIntentResult ->
                if (result.contents != null) {
                    val dados:Intent = Intent()
                    dados.putExtra("qrcode",result.contents)
                    setResult(RESULT_OK, dados)
                    finish()
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLeitorQrBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_leitor_qr)
        setContentView(binding.root)

        barcodeLauncher.launch(ScanOptions())
    }
}
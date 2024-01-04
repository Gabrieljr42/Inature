package com.example.inature3.data

import com.example.inature3.model.QrCode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MockQrsUser {

    lateinit var qrs:List<QrCode>

    init {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ENGLISH)
        qrs = listOf(
            QrCode(
                LocalDate.parse("15/08/2023",formatter),"Circuito Caminho do Passado",
            "Mirante","Prata"),
            QrCode(
                LocalDate.parse("10/07/2023",formatter),"Circuito das Águas",
                "Fauna2","Ouro"),
            QrCode(
                LocalDate.parse("06/07/2023",formatter),"Canela de Ema",
                "Fauna1","Bronze"),
            QrCode(
                LocalDate.parse("20/06/2023",formatter),"Paredão",
                "Geo-Plus","Prata"),
            QrCode(
                LocalDate.parse("14/06/2023",formatter),"Poção",
                "Riacho","Ouro")

        )
    }
}
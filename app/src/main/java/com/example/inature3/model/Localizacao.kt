package com.example.inature3.model

import java.util.Date

data class Localizacao (
    var lat:Double = 0.0,
    var lon:Double = 0.0,
    var tempo: Date? = null,
    var altitude:Double = 0.0,
    var caminhoId: Long = 0L,
    var id: Long = 0L){


}
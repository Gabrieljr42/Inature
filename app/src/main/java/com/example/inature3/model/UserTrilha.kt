package com.example.inature3.model

import java.time.LocalDate

data class UserTrilha(
    val emailUsuario: String,
    val id: Long,
    val tempoGasto: String,
    val nota: Int,
    var mtAndados:Double
)

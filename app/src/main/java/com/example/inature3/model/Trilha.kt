package com.example.inature3.model

data class Trilha (
    var id: Long,
    var nomeTrilha: String,
    var tempoEstimado: String,
    var dificuldade: Double,
    var distancia: Double,
    var lat: Double,
    var lng: Double
) {

}

package com.example.inature3.data

import com.example.inature3.model.Trilha
import com.example.inature3.model.UserRank
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MockRankUser {
    lateinit var ranks:List<UserRank>

    init {
        ranks = listOf(
            UserRank("avatar1","João",50000),
            UserRank("avatar2","Júlia",48000),
            UserRank("avatar3","Marina",45000),
            UserRank("avatar4","Pedro",35000),
            UserRank("avatar5","Saulo",30000),
            UserRank("avatar6","Carolina",29000),
            UserRank("avatar7","Bia",27000),
            UserRank("avatar8","Lu",25000),
            UserRank("avatar9","Lucas",15000),
            UserRank("avatar10","Thiago",14000),
            UserRank("avatar11","Letícia",12000),
            UserRank("avatar12","Vinícius",10000),
            UserRank("avatar13","Edmar",0),
            UserRank("avatar14","Regina",0),
            UserRank("avatar15","Leo",0),
        )
    }
}
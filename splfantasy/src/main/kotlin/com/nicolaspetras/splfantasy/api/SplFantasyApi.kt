package com.nicolaspetras.splfantasy.api

import org.springframework.web.bind.annotation.RestController

@RestController
class SplFantasyApi {

    fun getScores(): ArrayList<String> {
        return arrayListOf("hello", "how", "are", "you")
    }
}
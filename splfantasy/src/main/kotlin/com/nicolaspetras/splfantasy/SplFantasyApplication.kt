package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplFantasyApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyApplication>(*args)
    val stats = scrapeSplStats()
//    val scores = scoreStats(stats)
//    printScores(scores)
}


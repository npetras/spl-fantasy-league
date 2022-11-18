package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplFantasyApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyApplication>(*args)
    val scorer = Scorer()
    val stats = scrapeSplStats()
    val scores = scorer.scoreMatch(stats)
    printScores(scores)
}

fun printScores(matchScores: SplMatchScore) {
    println("${matchScores.homeTeamName} Team Scores: ")
    for (playerScore in matchScores.homeTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }

    println()
    println("${matchScores.awayTeamName} Team Scores: ")
    for (playerScore in matchScores.awayTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }
    println()
}

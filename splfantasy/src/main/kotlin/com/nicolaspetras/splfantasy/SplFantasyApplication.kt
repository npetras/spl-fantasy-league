package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.math.RoundingMode

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
    printTeamsScores(matchScores.homeTeamScores)
    println("\n${matchScores.awayTeamName} Team Scores: ")
    printTeamsScores(matchScores.awayTeamScores)
    println()
}

private fun printTeamsScores(teamScores: List<SplPlayerMatchScore>) {
    for (playerScore in teamScores) {
        println("${playerScore.name} ")
        playerScore.gameScores.forEach {
            print("${it.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()} ")
        }
        println("\t\t${playerScore.overallMatchScore().toBigDecimal().setScale(3, RoundingMode.UP)}")
    }
}

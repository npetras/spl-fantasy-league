package com.nicolaspetras.splfantasy.service

import com.nicolaspetras.splfantasy.model.scoring.output.SplPlayerSeasonScore
import com.nicolaspetras.splfantasy.model.scoring.internal.SplMatchScore
import com.nicolaspetras.splfantasy.model.scoring.internal.SplPlayerMatchScore
import java.math.RoundingMode

fun printSeasonScores(seasonScores: ArrayList<SplPlayerSeasonScore>) {
    for (player in seasonScores) {
        println("${player.splPlayer.name} ${player.splPlayer.role} ${player.splPlayer.team}")
        println(player.overallSeasonScore().setScale(3, RoundingMode.UP).toDouble())
    }
}

fun printMatchScoresList(matchScoresList: List<SplMatchScore>) {
    for (match in matchScoresList) {
        printMatchScores(match)
    }
}

fun printMatchScores(matchScores: SplMatchScore) {
    println("${matchScores.homeTeamName} Team Scores: ")
    printTeamsScores(matchScores.homeTeamScores)
    println("\n${matchScores.awayTeamName} Team Scores: ")
    printTeamsScores(matchScores.awayTeamScores)
    println()
}

fun printTeamsScores(teamScores: List<SplPlayerMatchScore>) {
    for (playerScore in teamScores) {
        println("${playerScore.name} ")
        playerScore.gameScores.forEach {
            print("${it.setScale(3, RoundingMode.UP).toDouble()} ")
        }
        println("\t\t${playerScore.overallMatchScore().setScale(3, RoundingMode.UP)}")
    }
}
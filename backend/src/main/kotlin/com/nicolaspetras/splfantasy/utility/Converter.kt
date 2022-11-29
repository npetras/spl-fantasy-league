package com.nicolaspetras.splfantasy.utility

import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore

fun convertMatchScoresToPlayerScore(matchScores: ArrayList<SplMatchScore>): ArrayList<SplPlayerMatchScore> {
    val playerMatchScores = arrayListOf<SplPlayerMatchScore>()
    for (matchScore in matchScores) {
        playerMatchScores.addAll(matchScore.homeTeamScores)
        playerMatchScores.addAll(matchScore.awayTeamScores)
    }
    return playerMatchScores
}
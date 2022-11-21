package com.nicolaspetras.splfantasy.model.score

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName

class SplPlayerMatchScore(
    val name: String = "",
    val role: SmiteRole = SmiteRole.NONE,
    val team: SplTeamName = SplTeamName.NONE,
    var gameScores: ArrayList<Double> = arrayListOf()
) {
    fun overallMatchScore(): Double {
        var cumulativeScore = 0.0
        if (gameScores.isNotEmpty()) {
            for (score in gameScores) {
                cumulativeScore += score
            }
        } else {
            println("Game scores are empty")
        }
        return cumulativeScore
    }
}
package com.nicolaspetras.splfantasy.model

class SplPlayerSeasonScore(
    val name: String,
    val team: SplTeamName,
    val role: SmiteRole,
    val matchScores: ArrayList<Double>
) {
    fun overallSeasonScore(): Double {
        var cumulativeScore = 0.0
        for (matchScore in matchScores) {
            cumulativeScore += matchScore
        }
        return cumulativeScore
    }
}
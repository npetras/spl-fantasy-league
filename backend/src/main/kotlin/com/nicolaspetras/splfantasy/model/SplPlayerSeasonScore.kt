package com.nicolaspetras.splfantasy.model

class SplPlayerSeasonScore(
    val splPlayer: SplPlayer,
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
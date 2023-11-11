package com.nicolaspetras.splfantasy.model.scoring.output

import com.nicolaspetras.splfantasy.model.SplPlayer
import java.math.BigDecimal

// TODO: Link with closer with SplPlayerMatchScore.
// TODO: Update to Data Class
class SplPlayerSeasonScore(
    val splPlayer: SplPlayer,
    // Replace with an ArrayList of ArrayList of Doubles
    // so that game scores are also retained
    val matchScores: ArrayList<BigDecimal>
) {
    /**
     * Returns the overall score for the season calculated from all the [matchScores]
     */
    // TODO: replace with assigned variable on creation of the data class
    fun overallSeasonScore(): BigDecimal {
        var cumulativeScore = BigDecimal("0.0")
        for (matchScore in matchScores) {
            cumulativeScore += matchScore
        }
        return cumulativeScore
    }
}
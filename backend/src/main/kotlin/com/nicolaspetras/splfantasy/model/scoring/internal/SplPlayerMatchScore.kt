package com.nicolaspetras.splfantasy.model.scoring.internal

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class SplPlayerMatchScore(
    // TODO: Change to SplPlayer -- remove name, role and team
    val name: String = "",
    val role: SmiteRole = SmiteRole.NONE,
    val team: SplTeamName = SplTeamName.NONE,
    var gameScores: ArrayList<BigDecimal> = arrayListOf()
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * Returns the overall match score for the player
     */
    fun overallMatchScore(): BigDecimal {
        var cumulativeScore = BigDecimal(0.0)
        if (gameScores.isNotEmpty()) {
            for (score in gameScores) {
                cumulativeScore += score
            }
        } else {
            log.warn("Game scores are empty")
        }
        return cumulativeScore
    }
}
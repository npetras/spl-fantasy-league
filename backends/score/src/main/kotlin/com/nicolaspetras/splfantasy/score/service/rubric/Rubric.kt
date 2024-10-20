package com.nicolaspetras.splfantasy.score.service.rubric

import com.nicolaspetras.splfantasy.score.model.SmiteRole
import com.nicolaspetras.splfantasy.score.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.score.model.statistics.SplGameStats
import com.nicolaspetras.splfantasy.score.model.statistics.SplPlayerStats
import com.nicolaspetras.splfantasy.score.service.points.PlayerPointsCalculator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

abstract class Rubric {
    private val log: Logger = LoggerFactory.getLogger(javaClass)



    // role specific point calculation for player statistics
    abstract val soloCalculator: PlayerPointsCalculator
    abstract val jungleCalculator: PlayerPointsCalculator
    abstract val midCalculator: PlayerPointsCalculator
    abstract val supportCalculator: PlayerPointsCalculator
    abstract val hunterCalculator: PlayerPointsCalculator

    /**
     * Returns the player's score. The score will be calculated differently for each role.
     */
    fun calculatePlayerScore(playerStats: SplPlayerStats): BigDecimal {
        return when(playerStats.splPlayer.role) {
            SmiteRole.SOLO -> soloCalculator.calculatePoints(playerStats)
            SmiteRole.JUNGLE -> jungleCalculator.calculatePoints(playerStats)
            SmiteRole.MID -> midCalculator.calculatePoints(playerStats)
            SmiteRole.SUPPORT -> supportCalculator.calculatePoints(playerStats)
            SmiteRole.CARRY -> hunterCalculator.calculatePoints(playerStats)
            else -> {
                log.error("Invalid role type provided: $playerStats.role")
                BigDecimal("0.0")
            }
        }
    }

    /**
     *
     */
    abstract fun calculateBonusPointsForGame(gameStats: SplGameStats, gameIndex: Int, homeTeamScores: ArrayList<SplPlayerMatchScore>, awayTeamScores: ArrayList<SplPlayerMatchScore>);
}
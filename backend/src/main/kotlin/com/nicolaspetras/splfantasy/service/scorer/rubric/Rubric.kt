package com.nicolaspetras.splfantasy.service.scorer.rubric

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.service.scorer.calculator.player.PlayerPointsCalculator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Rubric {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    abstract val soloCalculator: PlayerPointsCalculator
    abstract val jungleCalculator: PlayerPointsCalculator
    abstract val midCalculator: PlayerPointsCalculator
    abstract val supportCalculator: PlayerPointsCalculator
    abstract val hunterCalculator: PlayerPointsCalculator

    /**
     * Returns the player's score. The score will be calculated differently for each role.
     */
    fun calculatePlayerScore(playerStats: SplPlayerStats): Double {
        return when(playerStats.role) {
            SmiteRole.SOLO -> soloCalculator.calculatePoints(playerStats)
            SmiteRole.JUNGLE -> jungleCalculator.calculatePoints(playerStats)
            SmiteRole.MID -> midCalculator.calculatePoints(playerStats)
            SmiteRole.SUPPORT -> supportCalculator.calculatePoints(playerStats)
            SmiteRole.CARRY -> hunterCalculator.calculatePoints(playerStats)
            else -> {
                log.error("Invalid role type provided: $playerStats.role")
                -1.0
            }
        }
    }
}
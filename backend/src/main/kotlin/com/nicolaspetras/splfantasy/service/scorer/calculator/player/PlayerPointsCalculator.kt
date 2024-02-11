package com.nicolaspetras.splfantasy.service.scorer.calculator.player

import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.service.scorer.calculator.PointsCalculator
import java.math.BigDecimal

/**
 * Calculates all independent points for the player (does not include bonus points like first bloods, top kills, assists)
 */
class PlayerPointsCalculator(
    private val killPts: Double,
    private val deathPts: Double,
    private val assistPts: Double,
    private val damageMitigationPts: Double,
    private val playerDamagePts: Double,
    private val goldPerMinPts: Double,
    private val wardPts: Double,
    private val structureDamagePts: Double,
    private val deathlessPts: Double,
    private val healingPts: Double
): PointsCalculator {
    override fun calculatePoints(playerStats: SplPlayerStats): BigDecimal {
        val killsScore = BigDecimal(killPts.toString()) * BigDecimal(playerStats.kills)
        val deathsScore = BigDecimal(deathPts.toString()) * BigDecimal(playerStats.deaths)
        val assistScore = BigDecimal(assistPts.toString()) * BigDecimal(playerStats.assists)
        val damageMitigationScore = BigDecimal(damageMitigationPts.toString()) * BigDecimal(playerStats.mitigatedDamage)
        val playerDamageScore = BigDecimal(playerDamagePts.toString()) * BigDecimal(playerStats.playerDamage)
        val goldScore = BigDecimal(goldPerMinPts.toString()) * BigDecimal(playerStats.goldPerMin)
        val wardScore = BigDecimal(wardPts.toString()) * BigDecimal(playerStats.wards)
        val structureDamageScore = BigDecimal(structureDamagePts.toString()) * BigDecimal(playerStats.structureDamage)
        val deathlessScore = BigDecimal(if (playerStats.deaths == 0) deathlessPts.toString() else "0")
        val healingScore = BigDecimal(healingPts.toString()) * BigDecimal(playerStats.healing)
        return killsScore + deathsScore + assistScore + damageMitigationScore +
                playerDamageScore + goldScore + wardScore + structureDamageScore +
                deathlessScore + healingScore
    }
}
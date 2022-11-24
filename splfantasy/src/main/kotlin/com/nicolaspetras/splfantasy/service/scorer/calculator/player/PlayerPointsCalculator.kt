package com.nicolaspetras.splfantasy.service.scorer.calculator.player

import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.service.scorer.calculator.PointsCalculator

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
    override fun calculatePoints(playerStats: SplPlayerStats): Double {
        val killsScore = killPts * playerStats.kills
        val deathsScore = deathPts * playerStats.deaths
        val assistScore = assistPts * playerStats.assists
        val damageMitigationScore = damageMitigationPts * playerStats.mitigatedDamage
        val playerDamageScore = playerDamagePts * playerStats.playerDamage
        val goldScore = goldPerMinPts * playerStats.goldPerMin
        val wardScore = wardPts * playerStats.wards
        val structureDamageScore = structureDamagePts * playerStats.structureDamage
        val deathlessScore = if (playerStats.deaths == 0) deathlessPts else 0.0
        val healingScore = healingPts * playerStats.healing
        return killsScore + deathsScore + assistScore + damageMitigationScore +
                playerDamageScore + goldScore + wardScore + structureDamageScore +
                deathlessScore + healingScore
    }
}
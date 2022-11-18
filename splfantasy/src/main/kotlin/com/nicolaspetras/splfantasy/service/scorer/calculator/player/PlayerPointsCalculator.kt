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
        return ((killPts * playerStats.kills)
                + (deathPts * playerStats.deaths)
                + (assistPts * playerStats.assists)
                + (damageMitigationPts * playerStats.mitigatedDamage)
                + (playerDamagePts * playerStats.playerDamage)
                + (goldPerMinPts * playerStats.goldPerMin)
                + (wardPts * playerStats.wards)
                + (structureDamagePts * playerStats.structureDamage)
                + (if (playerStats.deaths == 0) deathlessPts else 0.0)
                + (healingPts * playerStats.healing))
    }
}
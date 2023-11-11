package com.nicolaspetras.splfantasy.service.scorer.rubric

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.service.scorer.rubric.OfficialRubricV1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OfficialRubricV1Test {

    private val officialRubricV1 = OfficialRubricV1()

    @Test
    fun `test solo laner stats - baskin ferrymen vs oni warriors game 1`() {
        // Stats from ONI WARRIORS vs STYX FERRYMEN set

        val baskinGame1Stats = SplPlayerStats(
            name = "Baskin",
            splTeam = SplTeamName.STYX,
            role = SmiteRole.SOLO,
            kills = 8,
            deaths = 3,
            assists = 0,
            goldPerMin = 544,
            playerDamage = 44292,
            mitigatedDamage = 28404,
            structureDamage = 1340,
            healing = 0,
            wards = 30
        )
        val baskinGame1Score = officialRubricV1.calculatePlayerScore(baskinGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(baskinGame1Stats.kills) * BigDecimal("2")) +
                    (BigDecimal(baskinGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(baskinGame1Stats.assists) * BigDecimal("0.5")) +
                    (BigDecimal(baskinGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(baskinGame1Stats.playerDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(baskinGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(baskinGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(baskinGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(baskinGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore, baskinGame1Score)
    }
}
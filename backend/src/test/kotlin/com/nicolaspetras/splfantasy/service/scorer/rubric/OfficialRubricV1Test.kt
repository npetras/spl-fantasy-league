package com.nicolaspetras.splfantasy.service.scorer.rubric

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Tests for the scoring calculation of the [OfficialRubricV1] scoring rubric.
 *
 * The stats for the test below were taken from "SMITE Pro League Road to Worlds Week 5 : Oni Warriors vs Styx Ferrymen"
 * Game 1 of that set, which was played on 21st October 2023.
 * YouTube link: https://www.youtube.com/watch?v=tdmIpzRiq40&t=9489s
 */
class OfficialRubricV1Test {

    private val officialRubricV1 = OfficialRubricV1()

    @Test
    fun `test solo laner stats - Baskin ferrymen vs warriors game 1`() {

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
    @Test
    fun `test mid laner stats - Paul ferrymen vs warriors game 1`() {

        val paulGame1Stats = SplPlayerStats(
            name = "Paul",
            splTeam = SplTeamName.STYX,
            role = SmiteRole.MID,
            kills = 0,
            deaths = 2,
            assists = 2,
            goldPerMin = 484,
            playerDamage = 11530,
            mitigatedDamage = 6037,
            structureDamage = 564,
            healing = 0,
            wards = 10
        )
        val paulGame1Score = officialRubricV1.calculatePlayerScore(paulGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(paulGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(paulGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(paulGame1Stats.assists) * BigDecimal("0.1")) +
                    (BigDecimal(paulGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(paulGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(paulGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(paulGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(paulGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(paulGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), paulGame1Score.setScale(5, RoundingMode.UP))
    }

    @Test
    fun `test carry stats - CycloneSpin ferrymen vs warriors game 1`() {

        val cycloneGame1Stats = SplPlayerStats(
            name = "CycloneSpin",
            splTeam = SplTeamName.STYX,
            role = SmiteRole.CARRY,
            kills = 0,
            deaths = 2,
            assists = 5,
            goldPerMin = 475,
            playerDamage = 20819,
            mitigatedDamage = 14333,
            structureDamage = 5007,
            healing = 0,
            wards = 7
        )
        val cycloneGame1Score = officialRubricV1.calculatePlayerScore(cycloneGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(cycloneGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(cycloneGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(cycloneGame1Stats.assists) * BigDecimal("0.1")) +
                    (BigDecimal(cycloneGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(cycloneGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(cycloneGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(cycloneGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(cycloneGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(cycloneGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), cycloneGame1Score.setScale(5, RoundingMode.UP))
    }
}
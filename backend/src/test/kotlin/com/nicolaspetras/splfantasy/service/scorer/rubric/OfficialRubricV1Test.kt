package com.nicolaspetras.splfantasy.service.scorer.rubric

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Tests for the scoring calculation of the [OfficialRubricV1] scoring rubric.
 *
 * The stats for the test below were taken from "SMITE Pro League Road to Worlds Week 5 : Oni Warriors vs Styx Ferrymen"
 * Game 1 of that set, which was played on 21st October 2023.
 * YouTube link for view of Game 1 stats: https://youtu.be/tdmIpzRiq40?si=qMzj-AUS4JNatp_0&t=4469
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
        println(Json.encodeToString(baskinGame1Stats))
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
        println(Json.encodeToString(paulGame1Stats))

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
        println(Json.encodeToString(cycloneGame1Stats))
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

    @Test
    fun `test jungle stats - Cyno ferrymen vs warriors game 1`() {
        val cynoGame1Stats = SplPlayerStats(
            name = "Cyno",
            splTeam = SplTeamName.STYX,
            role = SmiteRole.JUNGLE,
            kills = 0,
            deaths = 2,
            assists = 7,
            goldPerMin = 506,
            playerDamage = 12_935,
            mitigatedDamage = 24_198,
            structureDamage = 735,
            healing = 0,
            wards = 12
        )
        val cynoGame1Score = officialRubricV1.calculatePlayerScore(cynoGame1Stats)
        println(Json.encodeToString(cynoGame1Stats))
        val expectedScore: BigDecimal =
            (BigDecimal(cynoGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(cynoGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(cynoGame1Stats.assists) * BigDecimal("0.2")) +
                    (BigDecimal(cynoGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(cynoGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(cynoGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(cynoGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(cynoGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(cynoGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), cynoGame1Score.setScale(5, RoundingMode.UP))

    }

    @Test
    fun `test support stats - Aror ferrymen vs warriors game 1`() {

        val arorGame1Stats = SplPlayerStats(
            name = "Aror",
            splTeam = SplTeamName.STYX,
            role = SmiteRole.SUPPORT,
            kills = 2,
            deaths = 6,
            assists = 5,
            goldPerMin = 454,
            playerDamage = 9906,
            mitigatedDamage = 60833,
            structureDamage = 69,
            healing = 0,
            wards = 16
        )
        val arorGame1Score = officialRubricV1.calculatePlayerScore(arorGame1Stats)
        println(Json.encodeToString(arorGame1Stats))
        val expectedScore: BigDecimal =
            (BigDecimal(arorGame1Stats.kills) * BigDecimal("1.5")) +
                    (BigDecimal(arorGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(arorGame1Stats.assists) * BigDecimal("0.75")) +
                    (BigDecimal(arorGame1Stats.mitigatedDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(arorGame1Stats.playerDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(arorGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(arorGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(arorGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(arorGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), arorGame1Score.setScale(5, RoundingMode.UP))
    }

    @Test
    fun `test solo laner stats - SoloOrTroll ferrymen vs warriors game 1`() {

        val soloOrTrollGame1Stats = SplPlayerStats(
            name = "SoloOrTroll",
            splTeam = SplTeamName.ONI,
            role = SmiteRole.SOLO,
            kills = 1,
            deaths = 6,
            assists = 11,
            goldPerMin = 479,
            playerDamage = 19_574,
            mitigatedDamage = 72_253,
            structureDamage = 1_077,
            healing = 0,
            wards = 13
        )
        println(Json.encodeToString(soloOrTrollGame1Stats))
        val soloOrTrollGame1Score = officialRubricV1.calculatePlayerScore(soloOrTrollGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(soloOrTrollGame1Stats.kills) * BigDecimal("2")) +
                    (BigDecimal(soloOrTrollGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(soloOrTrollGame1Stats.assists) * BigDecimal("0.5")) +
                    (BigDecimal(soloOrTrollGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(soloOrTrollGame1Stats.playerDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(soloOrTrollGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(soloOrTrollGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(soloOrTrollGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(soloOrTrollGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore, soloOrTrollGame1Score)
    }

    @Test
    fun `test jungle stats - Panitom ferrymen vs warriors game 1`() {

        val panitomGame1Stats = SplPlayerStats(
            name = "Panitom",
            splTeam = SplTeamName.ONI,
            role = SmiteRole.JUNGLE,
            kills = 2,
            deaths = 2,
            assists = 6,
            goldPerMin = 496,
            playerDamage = 20_255,
            mitigatedDamage = 27_370,
            structureDamage = 320,
            healing = 0,
            wards = 19
        )
        println(Json.encodeToString(panitomGame1Stats))
        val panitomGame1Score = officialRubricV1.calculatePlayerScore(panitomGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(panitomGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(panitomGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(panitomGame1Stats.assists) * BigDecimal("0.2")) +
                    (BigDecimal(panitomGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(panitomGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(panitomGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(panitomGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(panitomGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(panitomGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), panitomGame1Score.setScale(5, RoundingMode.UP))
    }

    @Test
    fun `test mid laner stats - Pegon ferrymen vs warriors game 1`() {

        val pegonGame1Stats = SplPlayerStats(
            name = "Pegon",
            splTeam = SplTeamName.ONI,
            role = SmiteRole.MID,
            kills = 7,
            deaths = 0,
            assists = 7,
            goldPerMin = 554,
            playerDamage = 44_144,
            mitigatedDamage = 17_990,
            structureDamage = 1_877,
            healing = 302,
            wards = 12
        )
        println(Json.encodeToString(pegonGame1Stats))
        val pegonGame1Score = officialRubricV1.calculatePlayerScore(pegonGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(pegonGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(pegonGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(pegonGame1Stats.assists) * BigDecimal("0.1")) +
                    (BigDecimal(pegonGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(pegonGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(pegonGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(pegonGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(pegonGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(1) * BigDecimal("3.0")) + // deathless
                    (BigDecimal(pegonGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), pegonGame1Score.setScale(5, RoundingMode.UP))
    }

    @Test
    fun `test carry stats - Netrioid ferrymen vs warriors game 1`() {

        val netrioidGame1Stats = SplPlayerStats(
            name = "Netrioid",
            splTeam = SplTeamName.ONI,
            role = SmiteRole.CARRY,
            kills = 5,
            deaths = 1,
            assists = 7,
            goldPerMin = 497,
            playerDamage = 19_305,
            mitigatedDamage = 16_454,
            structureDamage = 8_455,
            healing = 0,
            wards = 16
        )
        println(Json.encodeToString(netrioidGame1Stats))
        val netrioidGame1Score = officialRubricV1.calculatePlayerScore(netrioidGame1Stats)
        val expectedScore: BigDecimal =
            (BigDecimal(netrioidGame1Stats.kills) * BigDecimal("2.5")) +
                    (BigDecimal(netrioidGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(netrioidGame1Stats.assists) * BigDecimal("0.1")) +
                    (BigDecimal(netrioidGame1Stats.mitigatedDamage) * BigDecimal("0.00005")) +
                    (BigDecimal(netrioidGame1Stats.playerDamage) * BigDecimal("0.0001")) +
                    (BigDecimal(netrioidGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(netrioidGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(netrioidGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(netrioidGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), netrioidGame1Score.setScale(5, RoundingMode.UP))
    }

    @Test
    fun `test support stats - Genetics ferrymen vs warriors game 1`() {

        val geneticsGame1Stats = SplPlayerStats(
            name = "Genetics",
            splTeam = SplTeamName.ONI,
            role = SmiteRole.SUPPORT,
            kills = 0,
            deaths = 1,
            assists = 12,
            goldPerMin = 458,
            playerDamage = 5_099,
            mitigatedDamage = 34_850,
            structureDamage = 1_050,
            healing = 9_566,
            wards = 21
        )
        val geneticsGame1Score = officialRubricV1.calculatePlayerScore(geneticsGame1Stats)
        println(Json.encodeToString(geneticsGame1Stats))
        val expectedScore: BigDecimal =
            (BigDecimal(geneticsGame1Stats.kills) * BigDecimal("1.5")) +
                    (BigDecimal(geneticsGame1Stats.deaths) * BigDecimal("-1")) +
                    (BigDecimal(geneticsGame1Stats.assists) * BigDecimal("0.75")) +
                    (BigDecimal(geneticsGame1Stats.mitigatedDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(geneticsGame1Stats.playerDamage) * BigDecimal("0.000075")) +
                    (BigDecimal(geneticsGame1Stats.goldPerMin) * BigDecimal("0.001")) +
                    (BigDecimal(geneticsGame1Stats.wards) * BigDecimal("0.1")) +
                    (BigDecimal(geneticsGame1Stats.structureDamage) * BigDecimal("0.00025")) +
                    (BigDecimal(0)) + // deathless
                    (BigDecimal(geneticsGame1Stats.healing) * BigDecimal("0.0001"))
        assertEquals(expectedScore.setScale(5, RoundingMode.UP), geneticsGame1Score.setScale(5, RoundingMode.UP))
    }
}
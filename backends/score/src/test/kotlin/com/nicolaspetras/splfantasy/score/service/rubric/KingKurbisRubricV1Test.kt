package com.nicolaspetras.splfantasy.score.service.rubric

import com.nicolaspetras.splfantasy.score.model.SmiteRole
import com.nicolaspetras.splfantasy.score.model.SplPlayer
import com.nicolaspetras.splfantasy.score.model.statistics.SplPlayerStats
import com.nicolaspetras.splfantasy.score.model.SplTeamName
import com.nicolaspetras.splfantasy.score.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.score.model.statistics.SplGameStats
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * The stats for the tests below were taken from game 1 of match "SMITE Pro League Road to Worlds Week 5 : Oni Warriors
 * vs Styx Ferrymen" played on 21st October 2023.
 * YouTube link with view of Game 1 stats: https://youtu.be/tdmIpzRiq40?si=qMzj-AUS4JNatp_0&t=4469
 * Game 2 Stats timestamp: https://youtu.be/tdmIpzRiq40?si=naB8ObFZaKOspATk&t=7409
 */
class KingKurbisRubricV1Test {
//    val pegonGame1Stats = SplPlayerStats(
//        splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
//        kills = 7,
//        deaths = 0,
//        assists = 7,
//            goldPerMin = 554,
//            playerDamage = 44_144,
//            mitigatedDamage = 17_990,
//            structureDamage = 1_877,
//            healing = 302,
//            wards = 12
//    )
//    val netrioidGame1Stats = SplPlayerStats(
//        splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
//        kills = 5,
//        deaths = 1,
//        assists = 7,
//            goldPerMin = 497,
//            playerDamage = 19_305,
//            mitigatedDamage = 16_454,
//            structureDamage = 8_455,
//            healing = 0,
//            wards = 16
//    )
//    val geneticsGame1Stats = SplPlayerStats(
//        splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
//        kills = 0,
//        deaths = 1,
//        assists = 12,
//            goldPerMin = 458,
//            playerDamage = 5_099,
//            mitigatedDamage = 34_850,
//            structureDamage = 1_050,
//            healing = 9_566,
//            wards = 21
//    )

    /**
     * Tests [KingKurbisRubricV1.awardTopAssistsGame]. Stats from Game 1
     */
    @Test
    fun `test bonus assist points per game for team - support with top assists on their team`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            assists = 7,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            assists = 7,
        )
        val geneticsGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
            assists = 12,
        )

        val teamPlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats, geneticsGame1Stats)
        // given gameScores
        val teamPlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("0.7"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("0.7"))),
            SplPlayerMatchScore(SplPlayer(name = "Genetics", SplTeamName.ONI, role = SmiteRole.SUPPORT), gameScores = arrayOf(BigDecimal("9.0")))
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopAssistsGame(playerStats = teamPlayerStats, playerScores = teamPlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("0.7"), teamPlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("0.7"), teamPlayerScores[1].gameScores[0])
        assertEquals(BigDecimal("10.0"), teamPlayerScores[2].gameScores[0])
    }

    /**
     * Tests [KingKurbisRubricV1.awardTopAssistsGame].  Stats from Game 1
     */
    @Test
    fun `test bonus assist points per game for team - support not top assists on team`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            assists = 2,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            assists = 4,
        )
        val geneticsGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
            assists = 2,
        )

        val teamPlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats, geneticsGame1Stats)
        // given gameScores
        val teamPlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("0.2"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("0.4"))),
            SplPlayerMatchScore(SplPlayer(name = "Genetics", SplTeamName.ONI, role = SmiteRole.SUPPORT), gameScores = arrayOf(BigDecimal("1.5")))
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopAssistsGame(playerStats = teamPlayerStats, playerScores = teamPlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("0.2"), teamPlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("0.4"), teamPlayerScores[1].gameScores[0])
        assertEquals(BigDecimal("1.5"), teamPlayerScores[2].gameScores[0])
    }

    /**
     * Tests [KingKurbisRubricV1.awardTopAssistsGame]. Stats from Game 2
     */
    @Test
    fun `test bonus assist points per game - support with top assists in game`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            assists = 2,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            assists = 4,
        )
        val geneticsGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
            assists = 2,
        )
        val arorGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Aror", team = SplTeamName.STYX, role = SmiteRole.SUPPORT),
            assists = 6
        )

        val gamePlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats, geneticsGame1Stats, arorGame1Stats)
        // given gameScores
        val gamePlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("0.2"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("0.4"))),
            SplPlayerMatchScore(SplPlayer(name = "Genetics", SplTeamName.ONI, role = SmiteRole.SUPPORT), gameScores = arrayOf(BigDecimal("1.5"))),
            SplPlayerMatchScore(SplPlayer(name = "Aror", team = SplTeamName.STYX, role = SmiteRole.SUPPORT), gameScores = arrayOf(BigDecimal("4.5")))
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopAssistsGame(playerStats = gamePlayerStats, playerScores = gamePlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("0.2"), gamePlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("0.4"), gamePlayerScores[1].gameScores[0])
        assertEquals(BigDecimal("1.5"), gamePlayerScores[2].gameScores[0])
        assertEquals(BigDecimal("5.5"), gamePlayerScores[3].gameScores[0])
    }

    /**
     * Tests [KingKurbisRubricV1.awardTopDamageGame]. Stats from Game 1
     */
    @Test
    fun `test top damage bonus points per game for team - Pegon has top damage`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            playerDamage = 44_144,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            playerDamage = 20_284,
        )

        val teamPlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats)
        // given gameScores
        val teamPlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("44.144"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("20.284"))),
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopDamageGame(playerStats = teamPlayerStats, playerScores = teamPlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("45.144"), teamPlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("20.284"), teamPlayerScores[1].gameScores[0])
    }

    /**
     * Tests [KingKurbisRubricV1.awardTopDamageGame]. Stats from Game 1
     */
    @Test
    fun `test top kill bonus points per game for team - mid laner has top kills`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            kills = 7,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            kills = 5,
        )

        val teamPlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats)
        // given gameScores
        val teamPlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("17.5"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("12.5"))),
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopKillsGame(playerStats = teamPlayerStats, playerScores = teamPlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("18.5"), teamPlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("12.5"), teamPlayerScores[1].gameScores[0])
    }

    /**
     * Tests [KingKurbisRubricV1.awardTopDamageGame]. Stats from Game 1
     */
    @Test
    fun `test top kill bonus points per game all players - solo laner has top kills`() {
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            kills = 7,
        )
        val netrioidGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Netrioid", team = SplTeamName.ONI, role = SmiteRole.CARRY),
            kills = 5,
        )
        val baskinGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Baskin", team = SplTeamName.STYX, role = SmiteRole.SOLO),
            kills = 8,
        )

        val gamePlayerStats = arrayListOf(pegonGame1Stats, netrioidGame1Stats, baskinGame1Stats)
        // given gameScores
        val gamePlayerScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(SplPlayer(name = "Pegon", SplTeamName.ONI, role = SmiteRole.MID), gameScores = arrayOf(BigDecimal("17.5"))),
            SplPlayerMatchScore(SplPlayer(name = "Netrioid", SplTeamName.ONI, role = SmiteRole.CARRY), gameScores = arrayOf(BigDecimal("12.5"))),
            SplPlayerMatchScore(SplPlayer(name = "Baskin", team = SplTeamName.STYX, role = SmiteRole.SOLO), gameScores = arrayOf(BigDecimal("16.0"))),
        )
        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.awardTopKillsGame(playerStats = gamePlayerStats, playerScores = gamePlayerScores, gameIndex = 0)

        // Genetics awarded an extra point for assist the others stay the same
        assertEquals(BigDecimal("17.5"), gamePlayerScores[0].gameScores[0])
        assertEquals(BigDecimal("12.5"), gamePlayerScores[1].gameScores[0])
        assertEquals(BigDecimal("18.0"), gamePlayerScores[2].gameScores[0])
    }

    /**
     * - Support on order team with top assists in game
     * - Support on chaos team top assists on team
     * - Mid laner on order team top damage on team
     * - Solo laner on chaos team top damage on team
     * - Solo laner on chaos team with top kills in game
     */
    @Test
    fun `test calculateBonusPointsForGame for a`() {
        // order team
        val pegonGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
            kills = 7,
            deaths = 0,
            assists = 7,
            playerDamage = 44_144
        )
        val geneticsGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
            kills = 0,
            deaths = 1,
            assists = 12,
            playerDamage = 5_099
        )

        // chaos team
        val baskinGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Baskin", team = SplTeamName.STYX, role = SmiteRole.SOLO),
            kills = 8,
            deaths = 3,
            assists = 0,
            playerDamage = 44_292
        )
        val arorGame1Stats = SplPlayerStats(
            splPlayer = SplPlayer(name = "Aror", team= SplTeamName.STYX, role = SmiteRole.SUPPORT),
            kills = 2,
            deaths = 6,
            assists = 5,
            playerDamage = 18_528
        )

        // game stats
        val gameStats = SplGameStats(
            orderTeamName = SplTeamName.ONI,
            chaosTeamName = SplTeamName.STYX,
            orderTeamPlayerStats = arrayListOf<SplPlayerStats>(pegonGame1Stats, geneticsGame1Stats),
            chaosTeamPlayerStats = arrayListOf<SplPlayerStats>(baskinGame1Stats, arorGame1Stats)
        )
        //home team scores
        val homeTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                splPlayer = SplPlayer(name = "Baskin", team = SplTeamName.STYX, role = SmiteRole.SOLO),
                gameScores = arrayOf(BigDecimal("17.4292"))),
            SplPlayerMatchScore(
                splPlayer = SplPlayer(name = "Aror", team= SplTeamName.STYX, role = SmiteRole.SUPPORT),
                gameScores = arrayOf(BigDecimal("2.1396"))
            )
        )
        // away team scores
        val awayTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                splPlayer = SplPlayer(name = "Pegon", team = SplTeamName.ONI, role = SmiteRole.MID),
                gameScores = arrayOf(BigDecimal("22.6144"))
            ),
            SplPlayerMatchScore(
                splPlayer = SplPlayer(name = "Genetics", team = SplTeamName.ONI, role = SmiteRole.SUPPORT),
                gameScores = arrayOf(BigDecimal("8.382425"))
            )
        )

        val kingKurbisRubricV1 = KingKurbisRubricV1()
        kingKurbisRubricV1.calculateBonusPointsForGame(gameStats = gameStats, gameIndex = 0, homeTeamScores = homeTeamScores, awayTeamScores = awayTeamScores)
        // bonus pt for top kills game, top kills game as solo, and top damage team
        assertEquals(BigDecimal("20.4292"), homeTeamScores[0].gameScores[0]) // baskin
        // top assist team (support)
        assertEquals(BigDecimal("3.1396"), homeTeamScores[1].gameScores[0]) // aror
        // top damage team
        assertEquals(BigDecimal("23.6144"), awayTeamScores[0].gameScores[0]) // pegon
        // top assist team & game (support)
        assertEquals(BigDecimal("10.382425"), awayTeamScores[1].gameScores[0]) // genetics

    }
}
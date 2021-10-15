package scoring

import data.SmiteRole
import data.SplTeamName
import data.collection.SplGameStats
import data.collection.SplMatchStats
import data.collection.SplPlayerStats
import data.extraction.SplMatchScore
import data.extraction.SplPlayerMatchScore
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.assertEquals

class ScorerTest {

    // test 2 game set
    @Test
    fun `test basic 2 game set - Kings vs Levis Phase 2 Week 4 Cut Down`() {

        val game1KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.MID,
                kills = 7,
                deaths = 1,
                assists = 8,
                playerDamage = 30598
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 5,
                assists = 9,
                playerDamage = 9747
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 1,
                assists = 15,
                playerDamage = 9288
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 2,
                assists = 8,
                playerDamage = 27527
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 1,
                assists = 11,
                playerDamage = 15212
            )
        )

        val game1LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 5,
                assists = 4,
                playerDamage = 16747
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 1,
                deaths = 5,
                assists = 5,
                playerDamage = 15270
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 3,
                assists = 5,
                playerDamage = 8996
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 3,
                deaths = 1,
                assists = 4,
                playerDamage = 27263
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 3,
                assists = 3,
                playerDamage = 22779
            )
        )

        val game1Stats = SplGameStats(
            orderTeamName = SplTeamName.KINGS,
            chaosTeamName = SplTeamName.LVTHN,
            orderTeamStats = game1KingsStats,
            chaosTeamStats = game1LvthnStats
        )

        val game2LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 2,
                assists = 15,
                playerDamage = 30369
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 7,
                deaths = 3,
                assists = 10,
                playerDamage = 18691
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 1,
                assists = 12,
                playerDamage = 13665
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 3,
                assists = 6,
                playerDamage = 43643
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 3,
                assists = 7,
                playerDamage = 27901
            )
        )

        val game2KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.MID,
                kills = 5,
                deaths = 2,
                assists = 5,
                playerDamage = 31177
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 4,
                assists = 6,
                playerDamage = 20120
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 5,
                assists = 10,
                playerDamage = 5133
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 4,
                assists = 2,
                playerDamage = 26204
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 4,
                assists = 4,
                playerDamage = 28465
            )
        )

        val game2Stats = SplGameStats(
            orderTeamName = SplTeamName.LVTHN,
            chaosTeamName = SplTeamName.KINGS,
            orderTeamStats = game2LvthnStats,
            chaosTeamStats = game2KingsStats
        )

        val splMatchStats = SplMatchStats(
            date = "30 July 2021",
            homeTeamName = SplTeamName.LVTHN,
            awayTeamName = SplTeamName.KINGS,
            homeTeamScore = 0,
            awayTeamScore = 2,
            games = arrayListOf(game1Stats, game2Stats)
        )

        val kingsTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                name = "BIGMANTINGZ",
                role = SmiteRole.MID,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(15.0, 9.0),
                overallMatchScore = 25.0
            ),
            SplPlayerMatchScore(
                name = "CAPTAINTWIG",
                role = SmiteRole.JUNGLE,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(-1.0, 0.0),
                overallMatchScore = 0.0
            ),
            SplPlayerMatchScore(
                name = "GENETICS",
                role = SmiteRole.SUPPORT,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(8.5, 1.0),
                overallMatchScore = 10.5
            ),
            SplPlayerMatchScore(
                name = "NETRIOID",
                role = SmiteRole.HUNTER,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(12.0, 4.0),
                overallMatchScore = 17.0
            ),
            SplPlayerMatchScore(
                name = "VARIETY",
                role = SmiteRole.SOLO,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(6.0, -0.5),
                overallMatchScore = 6.5
            )
        )

        val lvthnTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                name = "JARCORRR",
                role = SmiteRole.SOLO,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(-3.0, 7.0),
                overallMatchScore = 4.0
            ),
            SplPlayerMatchScore(
                name = "PANITOM",
                role = SmiteRole.JUNGLE,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(-3.0, 13.0),
                overallMatchScore = 10.0
            ),
            SplPlayerMatchScore(
                name = "RONNGYU",
                role = SmiteRole.SUPPORT,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(2.0, 6.5),
                overallMatchScore = 8.5
            ),
            SplPlayerMatchScore(
                name = "SHEENTO",
                role = SmiteRole.MID,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(6.0, 10.0),
                overallMatchScore = 16.0
            ),
            SplPlayerMatchScore(
                name = "ZAPMAN",
                role = SmiteRole.HUNTER,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(7.0, 5.0),
                overallMatchScore = 12.0
            )
        )

        val splMatchExpectedScores = SplMatchScore(
            homeTeamName = SplTeamName.LVTHN,
            awayTeamName = SplTeamName.KINGS,
            homeTeamScores = lvthnTeamScores,
            awayTeamScores = kingsTeamScores
        )

        val actualMatchScores = scoreMatch(splMatchStats)

        assertEquals(splMatchExpectedScores, actualMatchScores)
    }

    @Test
    fun `test basic 3 game set - Kings vs Levis Phase 2 Week 4`() {
        val game1KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.MID,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 5805
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 7580
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 3,
                assists = 0,
                playerDamage = 4736
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 7634
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 1,
                assists = 0,
                playerDamage = 8707
            )
        )

        val game1LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 0,
                assists = 9,
                playerDamage = 15999
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 4,
                deaths = 0,
                assists = 10,
                playerDamage = 10446
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 0,
                assists = 14,
                playerDamage = 6239
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 0,
                assists = 7,
                playerDamage = 16222
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 0,
                assists = 7,
                playerDamage = 9799
            )
        )

        val game1Stats = SplGameStats(
            orderTeamName = SplTeamName.KINGS,
            chaosTeamName = SplTeamName.LVTHN,
            orderTeamStats = game1KingsStats,
            chaosTeamStats = game1LvthnStats
        )

        val game2KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.MID,
                kills = 7,
                deaths = 1,
                assists = 8,
                playerDamage = 30598
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 5,
                assists = 9,
                playerDamage = 9747
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 1,
                assists = 15,
                playerDamage = 9288
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 2,
                assists = 8,
                playerDamage = 27527
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 1,
                assists = 11,
                playerDamage = 15212
            )
        )

        val game2LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 5,
                assists = 4,
                playerDamage = 16747
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 1,
                deaths = 5,
                assists = 5,
                playerDamage = 15270
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 3,
                assists = 5,
                playerDamage = 8996
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 3,
                deaths = 1,
                assists = 4,
                playerDamage = 27263
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 3,
                assists = 3,
                playerDamage = 22779
            )
        )

        val game2Stats = SplGameStats(
            orderTeamName = SplTeamName.KINGS,
            chaosTeamName = SplTeamName.LVTHN,
            orderTeamStats = game2KingsStats,
            chaosTeamStats = game2LvthnStats
        )

        val game3LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 2,
                assists = 15,
                playerDamage = 30369
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 7,
                deaths = 3,
                assists = 10,
                playerDamage = 18691
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 1,
                assists = 12,
                playerDamage = 13665
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 3,
                assists = 6,
                playerDamage = 43643
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = SplTeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 3,
                assists = 7,
                playerDamage = 27901
            )
        )

        val game3KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.MID,
                kills = 5,
                deaths = 2,
                assists = 5,
                playerDamage = 31177
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 4,
                assists = 6,
                playerDamage = 20120
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 5,
                assists = 10,
                playerDamage = 5133
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 4,
                assists = 2,
                playerDamage = 26204
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = SplTeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 4,
                assists = 4,
                playerDamage = 28465
            )
        )

        val game3Stats = SplGameStats(
            orderTeamName = SplTeamName.LVTHN,
            chaosTeamName = SplTeamName.KINGS,
            orderTeamStats = game3LvthnStats,
            chaosTeamStats = game3KingsStats
        )

        val splMatchStats = SplMatchStats(
            date = "30 July 2021",
            homeTeamName = SplTeamName.LVTHN,
            awayTeamName = SplTeamName.KINGS,
            homeTeamScore = 1,
            awayTeamScore = 2,
            games = arrayListOf(game1Stats, game2Stats, game3Stats)
        )

        val kingsTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                name = "BIGMANTINGZ",
                role = SmiteRole.MID,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(-4.0, 15.0, 9.0),
                overallMatchScore = 20.0
            ),
            SplPlayerMatchScore(
                name = "CAPTAINTWIG",
                role = SmiteRole.JUNGLE,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(-4.0, -1.0, 0.0),
                overallMatchScore = -5.0
            ),
            SplPlayerMatchScore(
                name = "GENETICS",
                role = SmiteRole.SUPPORT,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(-2.0, 8.5, 1.0),
                overallMatchScore = 7.5
            ),
            SplPlayerMatchScore(
                name = "NETRIOID",
                role = SmiteRole.HUNTER,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(-4.0, 12.0, 4.0),
                overallMatchScore = 12.0
            ),
            SplPlayerMatchScore(
                name = "VARIETY",
                role = SmiteRole.SOLO,
                team = SplTeamName.KINGS,
                gameScores = arrayListOf(0.0, 6.0, -0.5),
                overallMatchScore = 5.5
            )
        )

        val lvthnTeamScores = arrayListOf<SplPlayerMatchScore>(
            SplPlayerMatchScore(
                name = "JARCORRR",
                role = SmiteRole.SOLO,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(8.0, -3.0, 7.0),
                overallMatchScore = 12.0
            ),
            SplPlayerMatchScore(
                name = "PANITOM",
                role = SmiteRole.JUNGLE,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(12.0, -3.0, 13.0),
                overallMatchScore = 22.0
            ),
            SplPlayerMatchScore(
                name = "RONNGYU",
                role = SmiteRole.SUPPORT,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(13.0, 2.0, 6.5),
                overallMatchScore = 21.5
            ),
            SplPlayerMatchScore(
                name = "SHEENTO",
                role = SmiteRole.MID,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(17.0, 6.0, 10.0),
                overallMatchScore = 33.0
            ),
            SplPlayerMatchScore(
                name = "ZAPMAN",
                role = SmiteRole.HUNTER,
                team = SplTeamName.LVTHN,
                gameScores = arrayListOf(13.0, 7.0, 5.0),
                overallMatchScore = 25.0
            )
        )

        val splMatchExpectedScores = SplMatchScore(
            homeTeamName = SplTeamName.LVTHN,
            awayTeamName = SplTeamName.KINGS,
            homeTeamScores = lvthnTeamScores,
            awayTeamScores = kingsTeamScores
        )

        val actualMatchScores = scoreMatch(splMatchStats)

        assertEquals(splMatchExpectedScores, actualMatchScores)
    }

    // test 3 game set - multiple top damage team, multiple top assists team, multiple top kills games, multiple top
    // assists game
    @Test
    fun `test complex 3 game set - Kings vs Levis Phase 2 Week 4 Altered`() {

    }

}
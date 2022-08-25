package scoring

import data.SmiteRole
import data.TeamName
import data.collection.SplGameStats
import data.collection.SplMatchStats
import data.collection.SplPlayerStats
import data.scoring.MatchScore
import data.scoring.PlayerScore
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Test class for Scorer sources
 */
class ScorerTest {

    /**
     * Basic test for [scoreMatch] function, using data from a sample game in phase 2 week 4 of the
     * Smite Pro League 2021.
     */
    @Test
    fun `test basic 2 game sweep victory set - Kings vs Levis Phase 2 Week 4 Cut Down`() {

        val game1KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 7,
                deaths = 1,
                assists = 8,
                playerDamage = 30598
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 5,
                assists = 9,
                playerDamage = 9747
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 1,
                assists = 15,
                playerDamage = 9288
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 2,
                assists = 8,
                playerDamage = 27527
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
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
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 5,
                assists = 4,
                playerDamage = 16747
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 1,
                deaths = 5,
                assists = 5,
                playerDamage = 15270
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 3,
                assists = 5,
                playerDamage = 8996
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 3,
                deaths = 1,
                assists = 4,
                playerDamage = 27263
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 3,
                assists = 3,
                playerDamage = 22779
            )
        )

        val game1Stats = SplGameStats(
            orderTeamName = TeamName.KINGS,
            chaosTeamName = TeamName.LVTHN,
            orderTeamStats = game1KingsStats,
            chaosTeamStats = game1LvthnStats
        )

        val game2LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 2,
                assists = 15,
                playerDamage = 30369
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 7,
                deaths = 3,
                assists = 10,
                playerDamage = 18691
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 1,
                assists = 12,
                playerDamage = 13665
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 3,
                assists = 6,
                playerDamage = 43643
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
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
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 5,
                deaths = 2,
                assists = 5,
                playerDamage = 31177
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 4,
                assists = 6,
                playerDamage = 20120
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 5,
                assists = 10,
                playerDamage = 5133
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 4,
                assists = 2,
                playerDamage = 26204
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 4,
                assists = 4,
                playerDamage = 28465
            )
        )

        val game2Stats = SplGameStats(
            orderTeamName = TeamName.LVTHN,
            chaosTeamName = TeamName.KINGS,
            orderTeamStats = game2LvthnStats,
            chaosTeamStats = game2KingsStats
        )

        val splMatchStats = SplMatchStats(
            date = "30 July 2021",
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScore = 0,
            awayTeamScore = 2,
            games = arrayListOf(game1Stats, game2Stats)
        )

        val kingsTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "BIGMANTINGZ",
                role = SmiteRole.MID,
                team = TeamName.KINGS,
                gameScores = arrayListOf(19.0, 11.5),
                overallMatchScore = 33.5
            ),
            PlayerScore(
                name = "CAPTAINTWIG",
                role = SmiteRole.JUNGLE,
                team = TeamName.KINGS,
                gameScores = arrayListOf(3.5, 3.0),
                overallMatchScore = 9.5
            ),
            PlayerScore(
                name = "GENETICS",
                role = SmiteRole.SUPPORT,
                team = TeamName.KINGS,
                gameScores = arrayListOf(16.0, 6.0),
                overallMatchScore = 25.0
            ),
            PlayerScore(
                name = "NETRIOID",
                role = SmiteRole.HUNTER,
                team = TeamName.KINGS,
                gameScores = arrayListOf(17.0, 5.0),
                overallMatchScore = 25.0
            ),
            PlayerScore(
                name = "VARIETY",
                role = SmiteRole.SOLO,
                team = TeamName.KINGS,
                gameScores = arrayListOf(11.5, 1.5),
                overallMatchScore = 16.0
            )
        )

        val lvthnTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "JARCORRR",
                role = SmiteRole.SOLO,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(-1.0, 14.5),
                overallMatchScore = 13.5
            ),
            PlayerScore(
                name = "PANITOM",
                role = SmiteRole.JUNGLE,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(-0.5, 17.0),
                overallMatchScore = 16.5
            ),
            PlayerScore(
                name = "RONNGYU",
                role = SmiteRole.SUPPORT,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(4.0, 12.0),
                overallMatchScore = 16.0
            ),
            PlayerScore(
                name = "SHEENTO",
                role = SmiteRole.MID,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(8.0, 13.0),
                overallMatchScore = 21.0
            ),
            PlayerScore(
                name = "ZAPMAN",
                role = SmiteRole.HUNTER,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(8.5, 8.5),
                overallMatchScore = 17.0
            )
        )

        val splMatchExpectedScores = MatchScore(
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScores = lvthnTeamScores,
            awayTeamScores = kingsTeamScores
        )

        val actualMatchScores = scoreMatch(splMatchStats)

        assertEquals(splMatchExpectedScores, actualMatchScores)
    }

    /**
     * Basic test for [scoreMatch] function, using altered data from a sample game in phase 2 week 4 of the
     * Smite Pro League 2021.
     * This test makes sure to check that bonus points for sweep victories are added.
     */
    @Test
    fun `test basic 3 game set - Kings vs Levis Phase 2 Week 4`() {
        val game1KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 5805
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 7580
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 3,
                assists = 0,
                playerDamage = 4736
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 7634
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
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
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 0,
                assists = 9,
                playerDamage = 15999
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 4,
                deaths = 0,
                assists = 10,
                playerDamage = 10446
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 0,
                assists = 14,
                playerDamage = 6239
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 0,
                assists = 7,
                playerDamage = 16222
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 0,
                assists = 7,
                playerDamage = 9799
            )
        )

        val game1Stats = SplGameStats(
            orderTeamName = TeamName.KINGS,
            chaosTeamName = TeamName.LVTHN,
            orderTeamStats = game1KingsStats,
            chaosTeamStats = game1LvthnStats
        )

        val game2KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 7,
                deaths = 1,
                assists = 8,
                playerDamage = 30598
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 5,
                assists = 9,
                playerDamage = 9747
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 1,
                assists = 15,
                playerDamage = 9288
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 2,
                assists = 8,
                playerDamage = 27527
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
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
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 5,
                assists = 4,
                playerDamage = 16747
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 1,
                deaths = 5,
                assists = 5,
                playerDamage = 15270
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 3,
                assists = 5,
                playerDamage = 8996
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 3,
                deaths = 1,
                assists = 4,
                playerDamage = 27263
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 5,
                deaths = 3,
                assists = 3,
                playerDamage = 22779
            )
        )

        val game2Stats = SplGameStats(
            orderTeamName = TeamName.KINGS,
            chaosTeamName = TeamName.LVTHN,
            orderTeamStats = game2KingsStats,
            chaosTeamStats = game2LvthnStats
        )

        val game3LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 2,
                assists = 15,
                playerDamage = 30369
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 7,
                deaths = 3,
                assists = 10,
                playerDamage = 18691
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 1,
                assists = 12,
                playerDamage = 13665
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 3,
                assists = 6,
                playerDamage = 43643
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
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
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 5,
                deaths = 2,
                assists = 5,
                playerDamage = 31177
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 4,
                assists = 6,
                playerDamage = 20120
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 5,
                assists = 10,
                playerDamage = 5133
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 4,
                assists = 2,
                playerDamage = 26204
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 4,
                assists = 4,
                playerDamage = 28465
            )
        )

        val game3Stats = SplGameStats(
            orderTeamName = TeamName.LVTHN,
            chaosTeamName = TeamName.KINGS,
            orderTeamStats = game3LvthnStats,
            chaosTeamStats = game3KingsStats
        )

        val splMatchStats = SplMatchStats(
            date = "30 July 2021",
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScore = 1,
            awayTeamScore = 2,
            games = arrayListOf(game1Stats, game2Stats, game3Stats)
        )

        val kingsTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "BIGMANTINGZ",
                role = SmiteRole.MID,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-4.0, 15.0, 9.0),
                overallMatchScore = 22.0
            ),
            PlayerScore(
                name = "CAPTAINTWIG",
                role = SmiteRole.JUNGLE,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-4.0, -1.0, 0.0),
                overallMatchScore = -3.0
            ),
            PlayerScore(
                name = "GENETICS",
                role = SmiteRole.SUPPORT,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-3.0, 8.5, 1.0),
                overallMatchScore = 8.5
            ),
            PlayerScore(
                name = "NETRIOID",
                role = SmiteRole.HUNTER,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-4.0, 13.0, 4.0),
                overallMatchScore = 15.0
            ),
            PlayerScore(
                name = "VARIETY",
                role = SmiteRole.SOLO,
                team = TeamName.KINGS,
                gameScores = arrayListOf(0.0, 6.0, -0.5),
                overallMatchScore = 7.5
            )
        )
        val lvthnTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "JARCORRR",
                role = SmiteRole.SOLO,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(8.0, -3.0, 7.0),
                overallMatchScore = 13.0
            ),
            PlayerScore(
                name = "PANITOM",
                role = SmiteRole.JUNGLE,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(12.0, -3.0, 13.0),
                overallMatchScore = 23.0
            ),
            PlayerScore(
                name = "RONNGYU",
                role = SmiteRole.SUPPORT,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(13.0, 2.0, 6.5),
                overallMatchScore = 22.5
            ),
            PlayerScore(
                name = "SHEENTO",
                role = SmiteRole.MID,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(17.0, 6.0, 10.0),
                overallMatchScore = 34.0
            ),
            PlayerScore(
                name = "ZAPMAN",
                role = SmiteRole.HUNTER,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(13.0, 7.0, 5.0),
                overallMatchScore = 26.0
            )
        )
        val splMatchExpectedScores = MatchScore(
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScores = lvthnTeamScores,
            awayTeamScores = kingsTeamScores
        )

        val actualMatchScores = scoreMatch(splMatchStats)

        assertEquals(splMatchExpectedScores, actualMatchScores)
    }

    /**
     * More complex test for [scoreMatch] function, using altered data from a sample game in phase 2 week 4 of the
     * Smite Pro League 2021.
     * The data has been changed so that there are multiple instances of players having the same number of top assists,
     * kills, damage to ensure that if multiple players gain the top value for a stat they both get rewarded.
     */
    @Test
    fun `test complex 3 game set - Kings vs Levis Phase 2 Week 4 Altered`() {
        val game1KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 2,
                deaths = 4,
                assists = 0,
                playerDamage = 8707
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 0,
                deaths = 4,
                assists = 2,
                playerDamage = 7580
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 3,
                assists = 2,
                playerDamage = 4736
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 0,
                deaths = 4,
                assists = 0,
                playerDamage = 7634
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
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
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 2,
                deaths = 0,
                assists = 9,
                playerDamage = 15999
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 4,
                deaths = 0,
                assists = 14,
                playerDamage = 10446
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 0,
                assists = 14,
                playerDamage = 6239
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 0,
                assists = 7,
                playerDamage = 16222
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 6,
                deaths = 0,
                assists = 7,
                playerDamage = 9799
            )
        )
        val game1Stats = SplGameStats(
            orderTeamName = TeamName.KINGS,
            chaosTeamName = TeamName.LVTHN,
            orderTeamStats = game1KingsStats,
            chaosTeamStats = game1LvthnStats
        )

        val game2KingsStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "BIGMANTINGZ",
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 7,
                deaths = 1,
                assists = 8,
                playerDamage = 30598
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 5,
                assists = 9,
                playerDamage = 9747
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 1,
                assists = 15,
                playerDamage = 9288
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 2,
                assists = 8,
                playerDamage = 27527
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
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
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 0,
                deaths = 5,
                assists = 4,
                playerDamage = 16747
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 1,
                deaths = 5,
                assists = 5,
                playerDamage = 15270
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 3,
                assists = 5,
                playerDamage = 8996
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 3,
                deaths = 1,
                assists = 4,
                playerDamage = 27263
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.HUNTER,
                kills = 7,
                deaths = 3,
                assists = 3,
                playerDamage = 22779
            )
        )
        val game2Stats = SplGameStats(
            orderTeamName = TeamName.KINGS,
            chaosTeamName = TeamName.LVTHN,
            orderTeamStats = game2KingsStats,
            chaosTeamStats = game2LvthnStats
        )

        val game3LvthnStats = arrayListOf<SplPlayerStats>(
            SplPlayerStats(
                name = "JARCORRR",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 2,
                assists = 10,
                playerDamage = 30369
            ),
            SplPlayerStats(
                name = "PANITOM",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.JUNGLE,
                kills = 7,
                deaths = 3,
                assists = 10,
                playerDamage = 18691
            ),
            SplPlayerStats(
                name = "RONNGYU",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.SUPPORT,
                kills = 1,
                deaths = 1,
                assists = 12,
                playerDamage = 13665
            ),
            SplPlayerStats(
                name = "SHEENTO",
                splTeam = TeamName.LVTHN,
                role = SmiteRole.MID,
                kills = 6,
                deaths = 3,
                assists = 6,
                playerDamage = 43643
            ),
            SplPlayerStats(
                name = "ZAPMAN",
                splTeam = TeamName.LVTHN,
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
                splTeam = TeamName.KINGS,
                role = SmiteRole.MID,
                kills = 5,
                deaths = 2,
                assists = 5,
                playerDamage = 31177
            ),
            SplPlayerStats(
                name = "CAPTAINTWIG",
                splTeam = TeamName.KINGS,
                role = SmiteRole.JUNGLE,
                kills = 2,
                deaths = 4,
                assists = 6,
                playerDamage = 20120
            ),
            SplPlayerStats(
                name = "GENETICS",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SUPPORT,
                kills = 0,
                deaths = 5,
                assists = 12,
                playerDamage = 5133
            ),
            SplPlayerStats(
                name = "NETRIOID",
                splTeam = TeamName.KINGS,
                role = SmiteRole.HUNTER,
                kills = 4,
                deaths = 4,
                assists = 2,
                playerDamage = 26204
            ),
            SplPlayerStats(
                name = "VARIETY",
                splTeam = TeamName.KINGS,
                role = SmiteRole.SOLO,
                kills = 1,
                deaths = 4,
                assists = 4,
                playerDamage = 28465
            )
        )
        val game3Stats = SplGameStats(
            orderTeamName = TeamName.LVTHN,
            chaosTeamName = TeamName.KINGS,
            orderTeamStats = game3LvthnStats,
            chaosTeamStats = game3KingsStats
        )

        val splMatchStats = SplMatchStats(
            date = "30 July 2021",
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScore = 1,
            awayTeamScore = 2,
            games = arrayListOf(game1Stats, game2Stats, game3Stats)
        )

        val kingsTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "BIGMANTINGZ",
                role = SmiteRole.MID,
                team = TeamName.KINGS,
                gameScores = arrayListOf(1.0, 15.0, 9.0),
                overallMatchScore = 27.0
            ),
            PlayerScore(
                name = "CAPTAINTWIG",
                role = SmiteRole.JUNGLE,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-4.0, -1.0, 0.0),
                overallMatchScore = -3.0
            ),
            PlayerScore(
                name = "GENETICS",
                role = SmiteRole.SUPPORT,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-1.0, 8.5, 3.0),
                overallMatchScore = 12.5
            ),
            PlayerScore(
                name = "NETRIOID",
                role = SmiteRole.HUNTER,
                team = TeamName.KINGS,
                gameScores = arrayListOf(-4.0, 13.0, 4.0),
                overallMatchScore = 15.0
            ),
            PlayerScore(
                name = "VARIETY",
                role = SmiteRole.SOLO,
                team = TeamName.KINGS,
                gameScores = arrayListOf(0.0, 6.0, -0.5),
                overallMatchScore = 7.5
            )
        )
        val lvthnTeamScores = arrayListOf<PlayerScore>(
            PlayerScore(
                name = "JARCORRR",
                role = SmiteRole.SOLO,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(9.5, -3.0, 4.5),
                overallMatchScore = 12.0
            ),
            PlayerScore(
                name = "PANITOM",
                role = SmiteRole.JUNGLE,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(12.0, -3.0, 13.0),
                overallMatchScore = 23.0
            ),
            PlayerScore(
                name = "RONNGYU",
                role = SmiteRole.SUPPORT,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(13.0, 2.0, 8.5),
                overallMatchScore = 24.5
            ),
            PlayerScore(
                name = "SHEENTO",
                role = SmiteRole.MID,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(17.0, 6.0, 10.0),
                overallMatchScore = 34.0
            ),
            PlayerScore(
                name = "ZAPMAN",
                role = SmiteRole.HUNTER,
                team = TeamName.LVTHN,
                gameScores = arrayListOf(16.0, 12.0, 5.0),
                overallMatchScore = 34.0
            )
        )
        val splMatchExpectedScores = MatchScore(
            homeTeamName = TeamName.LVTHN,
            awayTeamName = TeamName.KINGS,
            homeTeamScores = lvthnTeamScores,
            awayTeamScores = kingsTeamScores
        )

        val actualMatchScores = scoreMatch(splMatchStats)

        assertEquals(splMatchExpectedScores, actualMatchScores)
    }

}
package com.nicolaspetras.splfantasy.service.scorer

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplPlayer
import com.nicolaspetras.splfantasy.model.scoring.output.SplPlayerSeasonScore
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.stat.collection.SplMatchStats
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.model.scoring.internal.SplMatchScore
import com.nicolaspetras.splfantasy.model.scoring.internal.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.service.scorer.rubric.OfficialRubricV1
import com.nicolaspetras.splfantasy.service.scorer.rubric.Rubric
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MatchScorer(private val rubric: Rubric = OfficialRubricV1()) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun scoreMatches(matchStatsList: List<SplMatchStats>): ArrayList<SplMatchScore> {
        val matchScores = arrayListOf<SplMatchScore>()
        for (match in matchStatsList) {
            matchScores.add(scoreMatch(match))
        }
        return matchScores
    }

    private fun scoreMatch(matchStats: SplMatchStats): SplMatchScore {
        val homeTeamScores = createHomeTeamScoresBase(matchStats)
        val awayTeamScores = createAwayTeamScoresBase(matchStats)

        for (game in matchStats.games) {
            scorePlayersOnTeamInGame(game.orderTeamPlayerStats, matchStats, homeTeamScores, awayTeamScores)
            scorePlayersOnTeamInGame(game.chaosTeamPlayerStats, matchStats, homeTeamScores, awayTeamScores)
            scoreBonusPoints(homeTeamScores, awayTeamScores)
        }

        return SplMatchScore(
            homeTeamName = matchStats.homeTeamName,
            awayTeamName = matchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores
        )
    }

    /**
     * Adds bonus points earned by each player in the game
     */
    private fun scoreBonusPoints(homeTeamScores: List<SplPlayerMatchScore>, awayTeamScores: List<SplPlayerMatchScore>) {
//        scoreTeamBonusPts(homeTeam)
        // scoreTeamBonusPts(awayTeam)
        // scoreGamePts
    }

    /**
     * Uses [teamStats] to score that team's players for a single game, storing those scores in the relevant list of
     * match scores -- home or away team.
     *
     * @param teamStats The stats for all members of the team for a single game
     * @param matchStats The stats for the whole match, contains the names of the home and away teams
     * @param homeTeamPlayerScores The match scores for the home team players
     * @param awayTeamPlayerScores The match scores for the away team players
     */
    private fun scorePlayersOnTeamInGame(
        teamStats: ArrayList<SplPlayerStats>,
        matchStats: SplMatchStats,
        homeTeamPlayerScores: ArrayList<SplPlayerMatchScore>,
        awayTeamPlayerScores: ArrayList<SplPlayerMatchScore>
    ) {
        // TODO: determine the team once at the start
        for (playerStats in teamStats) {
            val playerScore = rubric.calculatePlayerScore(playerStats)
            val teamScoresForCurrentPlayer =
                when (playerStats.splTeam) {
                    matchStats.homeTeamName -> {
                        homeTeamPlayerScores
                    }
                    matchStats.awayTeamName -> {
                        awayTeamPlayerScores
                    }
                    else -> {
                        log.error("Invalid team name: ${playerStats.splTeam}")
                        arrayListOf<SplPlayerMatchScore>()
                    }
                }
            val player = teamScoresForCurrentPlayer.find { it.name == playerStats.name }
            // TODO: log error if player not found in team
            player?.gameScores?.add(playerScore)
        }
    }

    /**
     * Returns a list of [SplPlayerMatchScore]s that encompasses the players on the home team,
     * without any scores, just the SplPlayer details
     */
    private fun createHomeTeamScoresBase(matchStats: SplMatchStats): ArrayList<SplPlayerMatchScore> {
        return createTeamScoresBase(
            orderTeamName = matchStats.games[0].orderTeamName,
            teamName = matchStats.homeTeamName,
            orderTeamStats = matchStats.games[0].orderTeamPlayerStats,
            chaosTeamStats = matchStats.games[0].chaosTeamPlayerStats
        )
    }

    /**
     * Returns a list of [SplPlayerMatchScore]s that encompasses the players on the away team,
     * without any scores, just the SplPlayer details
     */
    private fun createAwayTeamScoresBase(matchStats: SplMatchStats): ArrayList<SplPlayerMatchScore> {
        return createTeamScoresBase(
            orderTeamName = matchStats.games[0].orderTeamName,
            teamName = matchStats.awayTeamName,
            orderTeamStats = matchStats.games[0].orderTeamPlayerStats,
            chaosTeamStats = matchStats.games[0].chaosTeamPlayerStats
        )
    }

    /**
     * Returns a List of [SplPlayerMatchScore]s that is already populated with the name, role and team name of
     * each player for the [teamName] provided.
     *
     */
    private fun createTeamScoresBase(
        orderTeamName: SplTeamName,
        teamName: SplTeamName,
        chaosTeamStats: ArrayList<SplPlayerStats>,
        orderTeamStats: ArrayList<SplPlayerStats>,
    ): ArrayList<SplPlayerMatchScore> {
        val teamScores = arrayListOf<SplPlayerMatchScore>()

        if (orderTeamName == teamName) {
            for (playerStats in orderTeamStats) {
                teamScores.add(createSplPlayerMatchScore(playerStats))
            }
        } else {
            for (playerStats in chaosTeamStats) {
                teamScores.add(createSplPlayerMatchScore(playerStats))
            }
        }
        return teamScores
    }

    /**
     * Returns an [SplPlayerMatchScore] that is based on the [playerStats] provided.
     * The [SplPlayerMatchScore] will have the same name, role and team as [playerStats].
     */
    private fun createSplPlayerMatchScore(playerStats: SplPlayerStats): SplPlayerMatchScore {
        return SplPlayerMatchScore(
            name = playerStats.name,
            role = playerStats.role,
            team = playerStats.splTeam
        )
    }
}

fun combineScoresForEachPlayer(playerMatchScores: ArrayList<SplPlayerMatchScore>): ArrayList<SplPlayerSeasonScore> {
    val playerSeasonScores = hashMapOf<Pair<SmiteRole, SplTeamName>, SplPlayerSeasonScore>()
    for (playerScore in playerMatchScores) {
        val roleAndTeam = Pair(playerScore.role, playerScore.team)
        if (playerSeasonScores.contains(roleAndTeam)) {
            playerSeasonScores[roleAndTeam]?.matchScores?.add(playerScore.overallMatchScore())
        } else {
            val playerSeasonScore = SplPlayerSeasonScore(
                splPlayer = SplPlayer(name = playerScore.name, team = playerScore.team, role = playerScore.role),
                matchScores = arrayListOf(playerScore.overallMatchScore())
            )
            playerSeasonScores[roleAndTeam] = playerSeasonScore
        }
    }
    return ArrayList(playerSeasonScores.values)
}
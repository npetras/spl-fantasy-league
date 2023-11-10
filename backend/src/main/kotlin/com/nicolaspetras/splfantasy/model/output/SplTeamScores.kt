package com.nicolaspetras.splfantasy.model.output

import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.scoring.output.SplPlayerSeasonScore

/**
 * The scores of all the players in an SPL Team.
 * Might be more than five players if there have been roster changes.
 *
 * @param teamName The name of the Team from list defined in [SplTeamName]
 * @param playerScores The scores of all the players that have played for the team
 */
data class SplTeamScores(
    val teamName: SplTeamName,
    val playerScores: ArrayList<SplPlayerSeasonScore>
)

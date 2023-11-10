package com.nicolaspetras.splfantasy.model.scoring.output

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName

/**
 * Scores a Fantasy Player's Team, includes the Season Scores for each player and the overall team score.
 */
data class FantasyTeamScores(
    val playerName: String,
    val group: FantasyTeamGroupName,
    val solo: SplPlayerSeasonScore,
    val jungle: SplPlayerSeasonScore,
    val mid: SplPlayerSeasonScore,
    val support: SplPlayerSeasonScore,
    val hunter: SplPlayerSeasonScore
) {
    val overallTeamScore = solo.overallSeasonScore() +
            jungle.overallSeasonScore() +
            mid.overallSeasonScore() +
            support.overallSeasonScore() +
            hunter.overallSeasonScore()
}
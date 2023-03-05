package com.nicolaspetras.splfantasy.model.score

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.score.SplPlayerSeasonScore

class SplFantasyTeamScores(
    val playerName: String,
    val group: FantasyTeamGroupName,
    val solo: SplPlayerSeasonScore,
    val jungle: SplPlayerSeasonScore,
    val mid: SplPlayerSeasonScore,
    val support: SplPlayerSeasonScore,
    val hunter: SplPlayerSeasonScore
) {
    fun overallTeamScore(): Double {
        return solo.overallSeasonScore() +
                jungle.overallSeasonScore() +
                mid.overallSeasonScore() +
                support.overallSeasonScore() +
                hunter.overallSeasonScore()
    }
}
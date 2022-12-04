package com.nicolaspetras.splfantasy.model

class SplFantasyTeamScores(
    val playerName: String,
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
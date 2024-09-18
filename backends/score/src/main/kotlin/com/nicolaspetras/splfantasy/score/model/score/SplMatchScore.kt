package com.nicolaspetras.splfantasy.score.model.score

import com.nicolaspetras.splfantasy.score.model.SplTeamName

/**
 *
 */
data class SplMatchScore(
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScores: ArrayList<SplPlayerMatchScore>,
    val awayTeamScores: ArrayList<SplPlayerMatchScore>
)

package com.nicolaspetras.splfantasy.model.score

import com.nicolaspetras.splfantasy.model.SplTeamName

data class SplMatchScore(
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScores: ArrayList<SplPlayerMatchScore>,
    val awayTeamScores: ArrayList<SplPlayerMatchScore>
)

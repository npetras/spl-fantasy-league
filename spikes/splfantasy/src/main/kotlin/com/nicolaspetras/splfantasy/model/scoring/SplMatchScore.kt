package com.nicolaspetras.splfantasy.model.scoring

import com.nicolaspetras.splfantasy.model.SplTeamName

data class SplMatchScore(
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val homeTeamScores: ArrayList<SplPlayerMatchScore>,
    val awayTeamScores: ArrayList<SplPlayerMatchScore>
)

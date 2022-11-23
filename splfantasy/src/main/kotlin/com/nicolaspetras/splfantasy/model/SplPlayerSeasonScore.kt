package com.nicolaspetras.splfantasy.model

import com.nicolaspetras.splfantasy.model.score.SplMatchScore

class SplPlayerSeasonScore(
    val name: String,
    val team: SplTeamName,
    val role: SmiteRole,
    val matchScores: List<SplMatchScore>
) {
}
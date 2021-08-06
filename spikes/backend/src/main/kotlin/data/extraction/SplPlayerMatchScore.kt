package data.extraction

import data.SmiteRole
import data.SplTeamName

data class SplPlayerMatchScore(
    val name: String = "",
    val role: SmiteRole = SmiteRole.NONE,
    val team: SplTeamName = SplTeamName.NONE,
    var gameScores: ArrayList<Double> = arrayListOf(),
    var overallMatchScore: Double = 0.0
)
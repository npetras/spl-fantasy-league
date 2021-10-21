package data.extraction

import data.SmiteRole
import data.SplTeamName

data class SplPlayerMatchScore(
    val name: String = "",
    val role: SmiteRole = SmiteRole.NONE,
    val team: SplTeamName = SplTeamName.NONE,
    var gameScores: ArrayList<Double> = arrayListOf(),
    var overallMatchScore: Double = 0.0
) {
    fun calculateOverallMatchScore() {
        if (gameScores.isNotEmpty()) {
            var cumulativeScore = 0.0
            for (score in gameScores) {
                cumulativeScore += score
            }
            this.overallMatchScore = cumulativeScore
        } else {
            println("Game scores are empty")
        }
    }
}
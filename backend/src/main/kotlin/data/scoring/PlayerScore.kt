package data.scoring

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import data.SmiteRole
import data.TeamName

val log: Logger = LoggerFactory.getLogger("SplPlayerMatchScore")

data class PlayerScore(
    val name: String = "",
    val role: SmiteRole = SmiteRole.NONE,
    val team: TeamName = TeamName.NONE,
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
            log.info("Game scores are empty")
        }
    }
}
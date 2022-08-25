package data.scoring

import data.TeamName

data class MatchScore(
    val homeTeamName: TeamName,
    val awayTeamName: TeamName,
    val homeTeamScores: ArrayList<PlayerScore>,
    val awayTeamScores: ArrayList<PlayerScore>
)

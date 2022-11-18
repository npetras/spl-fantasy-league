package data.scoring

import data.SplTeamName

data class SplMatchScore(
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScores: ArrayList<SplPlayerMatchScore>,
    val awayTeamScores: ArrayList<SplPlayerMatchScore>
)

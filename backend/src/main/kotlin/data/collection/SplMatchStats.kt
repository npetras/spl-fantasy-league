package data.collection

import data.TeamName
import kotlin.collections.ArrayList

data class SplMatchStats(
    val date: String,
    val homeTeamName: TeamName,
    val awayTeamName: TeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val games: ArrayList<SplGameStats>
) {
}
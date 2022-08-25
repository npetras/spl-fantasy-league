package data.collection

import data.TeamName

data class SplGameStats(
    val orderTeamName: TeamName,
    val chaosTeamName: TeamName,
    val orderTeamStats: ArrayList<SplPlayerStats>,
    val chaosTeamStats: ArrayList<SplPlayerStats>
) {
}
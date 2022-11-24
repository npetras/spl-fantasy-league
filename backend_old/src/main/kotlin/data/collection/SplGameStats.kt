package data.collection

import data.SplTeamName

data class SplGameStats(
    val orderTeamName: SplTeamName,
    val chaosTeamName: SplTeamName,
    val orderTeamStats: ArrayList<SplPlayerStats>,
    val chaosTeamStats: ArrayList<SplPlayerStats>
) {
}
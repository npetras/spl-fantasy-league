package data.collection

import data.SplTeamName

data class SplGame(
    val orderTeamName: SplTeamName,
    val chaosTeamName: SplTeamName,
    val orderTeamStats: ArrayList<SplPlayerStats>,
    val chaosTeamStats: ArrayList<SplPlayerStats>
) {
}
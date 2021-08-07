package data.collection

import data.SplTeamName

data class SplGame(
    val orderTeamName: SplTeamName,
    val chaosTeamName: SplTeamName,
    val winningTeamStats: ArrayList<SplPlayerStats>,
    val losingTeamStats: ArrayList<SplPlayerStats>
) {
}
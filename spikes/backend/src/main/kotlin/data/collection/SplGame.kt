package data.collection

import data.SplTeamName

data class SplGame(
    val winningTeamStats: ArrayList<SplPlayerStats>,
    val losingTeamStats: ArrayList<SplPlayerStats>
) {
}
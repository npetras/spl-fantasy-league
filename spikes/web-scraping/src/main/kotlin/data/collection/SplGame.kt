package data.collection

import data.SplTeamName

data class SplGame(
    val team1Stats: ArrayList<SplPlayerStats>,
    val team2Stats: ArrayList<SplPlayerStats>
) {
}
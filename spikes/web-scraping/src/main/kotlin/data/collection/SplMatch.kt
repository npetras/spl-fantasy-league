package data.collection

import data.SplTeamName
import java.time.LocalDate
import java.util.Date
import kotlin.collections.ArrayList

data class SplMatch(
    val date: String,
    val team1: SplTeamName,
    val team2: SplTeamName,
    val team1Score: Int,
    val team2Score: Int,
    val games: ArrayList<SplGame>
) {
}
package data.collection

import data.SplTeamName
import java.time.LocalDate
import java.util.Date
import kotlin.collections.ArrayList

data class SplMatch(
    val date: String,
    val homeTeam: SplTeamName,
    val awayTeam: SplTeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val games: ArrayList<SplGame>
) {
}
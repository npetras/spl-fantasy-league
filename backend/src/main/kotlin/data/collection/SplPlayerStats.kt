package data.collection

import data.SmiteRole
import data.TeamName

data class SplPlayerStats(
    val name: String = "",
    val splTeam: TeamName = TeamName.NONE,
    val role: SmiteRole = SmiteRole.NONE,
    val kills: Int = -1,
    val deaths: Int = -1,
    val assists: Int = -1,
    var playerDamage: Int = -1
) {

}
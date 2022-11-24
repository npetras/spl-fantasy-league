package com.nicolaspetras.splfantasy.model.collection

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName

data class SplPlayerStats(
    val name: String = "",
    val splTeam: SplTeamName = SplTeamName.NONE,
    val role: SmiteRole = SmiteRole.NONE,
    val kills: Int = -1,
    val deaths: Int = -1,
    val assists: Int = -1,
    var mitigatedDamage: Int = -1,
    var playerDamage: Int = -1,
    var goldPerMin: Int = -1,
    var wards: Int = -1,
    var structureDamage: Int = -1,
    var healing: Int = -1
) {

}
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
    val mitigatedDamage: Int = -1,
    val playerDamage: Int = -1,
    val goldPerMin: Int = -1,
    val wards: Int = -1,
    val structureDamage: Int = -1,
    val healing: Int = -1
) {

}
package com.nicolaspetras.splfantasy.model.stat.collection

import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplTeamName
import kotlinx.serialization.Serializable

@Serializable
data class SplPlayerStats(
    // TODO: replace name, splTeam, role with SplPlayer
    val name: String = "",
    val splTeam: SplTeamName = SplTeamName.NONE,
    val role: SmiteRole = SmiteRole.NONE,
    val kills: Int = 0,
    val deaths: Int = 0,
    val assists: Int = 0,
    var mitigatedDamage: Int = 0,
    var playerDamage: Int = 0,
    var goldPerMin: Int = 0,
    var wards: Int = 0,
    var structureDamage: Int = 0,
    var healing: Int = 0
) {

}
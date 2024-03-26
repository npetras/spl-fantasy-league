package com.nicolaspetras.splfantasy.webscraper.model

data class SplPlayerStats(
    // TODO: replace name, splTeam, role with SplPlayer
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
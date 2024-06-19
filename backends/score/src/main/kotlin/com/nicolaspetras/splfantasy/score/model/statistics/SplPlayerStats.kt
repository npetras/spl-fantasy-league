package com.nicolaspetras.splfantasy.score.model.statistics

data class SplPlayerStats(
    val splPlayer: SplPlayer = SplPlayer(),
    val kills: Int = 0,
    val deaths: Int = 0,
    val assists: Int = 0,
    var mitigatedDamage: Int = 0,
    var playerDamage: Int = 0,
    var goldPerMin: Int = 0,
    var wards: Int = 0,
    var structureDamage: Int = 0,
    var healing: Int = 0
)

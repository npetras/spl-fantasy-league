package com.nicolaspetras.splfantasy.score.model.statistics

import com.nicolaspetras.splfantasy.score.model.SplPlayer

/**
 * Represents the statistics for a particular [SplPlayer] -- currently only used to represent the [SplPlayer]'s
 * statistics for a single game, but is not restricted to this use case.
 */
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

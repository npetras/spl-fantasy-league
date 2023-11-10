package com.nicolaspetras.splfantasy.model.output

/**
 * An SPL Fantasy Team that was drafted by Fantasy Player that is part of one of the
 * Fantasy Team Groups
 */
data class FantasyTeam(
    val fantasyPlayerName: String,
    // TODO: Update to FantasyTeamGroupName from String
    val fantasyTeamGroup: String,
    val solo: String,
    val soloScore: Double,
    val jungle: String,
    val jungleScore: Double,
    val mid: String,
    val midScore: Double,
    val support: String,
    val supportScore: Double,
    val hunter: String,
    val hunterScore: Double,
    val totalTeamScore: Double
)

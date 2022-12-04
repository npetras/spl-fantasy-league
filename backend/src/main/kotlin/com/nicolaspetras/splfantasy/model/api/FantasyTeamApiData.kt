package com.nicolaspetras.splfantasy.model.api

data class FantasyTeamApiData(
    val fantasyPlayerName: String,
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

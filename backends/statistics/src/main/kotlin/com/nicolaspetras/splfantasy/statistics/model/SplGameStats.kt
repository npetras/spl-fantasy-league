package com.nicolaspetras.splfantasy.statistics.model

/**
 * The scraped stats for a particular game with [orderTeamName] and [orderTeamPlayerStats] (team with first pick playing
 * on the order side), and the [chaosTeamName] and [orderTeamPlayerStats] (second pick team, on chaos side).
 */
data class SplGameStats(
    val orderTeamName: SplTeamName,
    val chaosTeamName: SplTeamName,
    val orderTeamPlayerStats: ArrayList<SplPlayerStats>,
    val chaosTeamPlayerStats: ArrayList<SplPlayerStats>
) {
}
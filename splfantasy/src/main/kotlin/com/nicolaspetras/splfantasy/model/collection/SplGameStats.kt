package com.nicolaspetras.splfantasy.model.collection

import com.nicolaspetras.splfantasy.model.SplTeamName

/**
 *
 */
data class SplGameStats(
    val orderTeamName: SplTeamName,
    val chaosTeamName: SplTeamName,
    val orderTeamStats: ArrayList<SplPlayerStats>,
    val chaosTeamStats: ArrayList<SplPlayerStats>
) {
}
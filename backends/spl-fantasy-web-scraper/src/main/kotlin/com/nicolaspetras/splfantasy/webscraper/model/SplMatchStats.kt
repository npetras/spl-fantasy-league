package com.nicolaspetras.splfantasy.webscraper.model

import kotlin.collections.ArrayList

data class SplMatchStats(
    val date: String,
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val games: ArrayList<SplGameStats>
) {
}
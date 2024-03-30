package com.nicolaspetras.splfantasy.statistics.model

import org.springframework.data.mongodb.core.mapping.Document
import kotlin.collections.ArrayList

@Document("splMatchStats")
data class SplMatchStats(
    val date: String,
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val games: ArrayList<SplGameStats>
) {
}
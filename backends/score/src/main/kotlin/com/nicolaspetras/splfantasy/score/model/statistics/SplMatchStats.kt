package com.nicolaspetras.splfantasy.score.model.statistics

import com.nicolaspetras.splfantasy.score.model.SplTeamName
import java.time.LocalDate

/**
 * Represents the statistics for a single SPL match, which includes metadata and match data.
 *
 * @param date Native JVM Date Type
 * @param originalDate Date scraped from web in String format
 * @param splSplitOrTournament The SPL split or tournament during which the match took place e.g. Summer Split, Smite Masters,
 * Road to Worlds, Smite World Championship.
 * @param homeTeamName The name of the home team as enum
 * @param awayTeamName The name of the away team as enum
 * @param homeTeamScore The match score for the home team
 * @param awayTeamScore The match score for the away team
 * @param games The statistics of the games that took place in the match
 */
data class SplMatchStats(
    // TODO: add time too for multiple matches between teams in one day?
    val date: LocalDate,
    val originalDate: String,
    val splSplitOrTournament: String,
    val homeTeamName: SplTeamName,
    val awayTeamName: SplTeamName,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val games: ArrayList<SplGameStats>
) {
}
/**
 * Functions for converting between data structures
 */
package com.nicolaspetras.splfantasy.utility

import com.nicolaspetras.splfantasy.model.SplFantasyTeamScores
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore
import java.math.RoundingMode

/**
 * Returns all the player scores for the match in one [ArrayList]
 */
fun convertMatchScoresToPlayerScore(matchScores: ArrayList<SplMatchScore>): ArrayList<SplPlayerMatchScore> {
    val playerMatchScores = arrayListOf<SplPlayerMatchScore>()
    for (matchScore in matchScores) {
        playerMatchScores.addAll(matchScore.homeTeamScores)
        playerMatchScores.addAll(matchScore.awayTeamScores)
    }
    return playerMatchScores
}

fun convertFantasyTeamScoresToApiData(fantasyTeamScores: ArrayList<SplFantasyTeamScores>): ArrayList<FantasyTeamApiData> {
    val fantasyTeamApiDataList = arrayListOf<FantasyTeamApiData>()
    for (score in fantasyTeamScores) {
        val fantasyTeamApiData = FantasyTeamApiData(
            fantasyPlayerName = score.playerName,
            solo = score.solo.splPlayer.name,
            soloScore = score.solo.overallSeasonScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble(),
            jungle = score.jungle.splPlayer.name,
            jungleScore = score.jungle.overallSeasonScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble(),
            mid = score.mid.splPlayer.name,
            midScore = score.mid.overallSeasonScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble(),
            support = score.support.splPlayer.name,
            supportScore = score.support.overallSeasonScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble(),
            hunter = score.hunter.splPlayer.name,
            hunterScore = score.hunter.overallSeasonScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble(),
            totalTeamScore = score.overallTeamScore().toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        )
        fantasyTeamApiDataList.add(fantasyTeamApiData)
    }
    fantasyTeamApiDataList.sortBy { it.totalTeamScore }
    fantasyTeamApiDataList.reverse()
    return fantasyTeamApiDataList
}
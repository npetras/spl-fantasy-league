/**
 * Functions for converting between data structures
 */
package com.nicolaspetras.splfantasy.utility

import com.nicolaspetras.splfantasy.model.SplFantasyTeamScores
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore

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
            soloScore = score.solo.overallSeasonScore(),
            jungle = score.jungle.splPlayer.name,
            jungleScore = score.jungle.overallSeasonScore(),
            mid = score.mid.splPlayer.name,
            midScore = score.mid.overallSeasonScore(),
            support = score.support.splPlayer.name,
            supportScore = score.support.overallSeasonScore(),
            hunter = score.hunter.splPlayer.name,
            hunterScore = score.hunter.overallSeasonScore(),
            totalTeamScore = score.overallTeamScore()
        )
        fantasyTeamApiDataList.add(fantasyTeamApiData)
    }
    return fantasyTeamApiDataList
}
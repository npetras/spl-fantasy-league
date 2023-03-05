/**
 * Functions for converting between data structures
 */
package com.nicolaspetras.splfantasy.utility

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.api.FantasyGroupApiData
import com.nicolaspetras.splfantasy.model.score.SplFantasyTeamScores
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

/**
 * Returns the list of all fantasy team scores to a data format that is suitable for this service's API.
 * Converting the internal format used in this service to a more basic format suitable for transmission to the frontend.
 */
fun convertFantasyTeamScoresToOverallScoresForApi(
    fantasyTeamScores: ArrayList<SplFantasyTeamScores>
): ArrayList<FantasyTeamApiData> {
    val fantasyTeamApiDataList = arrayListOf<FantasyTeamApiData>()
    for (score in fantasyTeamScores) {
        val fantasyTeamApiData = FantasyTeamApiData(
            fantasyPlayerName = score.playerName,
            fantasyTeamGroup = score.group.toString(),
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

/**
 * Returns the list off all fantasy team groups and the
 */
fun covertFantasyTeamScoresToGroupScoresForApi(
    fantasyTeamScores: ArrayList<SplFantasyTeamScores>
): ArrayList<FantasyGroupApiData> {
    val allTeamScores = convertFantasyTeamScoresToOverallScoresForApi(fantasyTeamScores)
    val existingGroups = arrayListOf<String>()
    val groupsScores = arrayListOf<FantasyGroupApiData>()

    for (teamScores in allTeamScores) {
        if (existingGroups.contains(teamScores.fantasyTeamGroup)) {
            val relevantGroup = groupsScores.find { it.groupName == teamScores.fantasyTeamGroup }
            relevantGroup?.fantasyTeams?.add(teamScores)
        } else {
            groupsScores.add(
                FantasyGroupApiData(
                    teamScores.fantasyTeamGroup,
                    arrayListOf(teamScores)
                )
            )
            existingGroups.add(teamScores.fantasyTeamGroup)
        }
    }
    groupsScores.sortBy { it.groupName }
    return groupsScores
}
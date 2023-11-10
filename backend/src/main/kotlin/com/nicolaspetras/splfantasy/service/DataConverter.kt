/**
 * Functions for converting between data structures
 */
package com.nicolaspetras.splfantasy.service

import com.nicolaspetras.splfantasy.model.output.FantasyGroup
import com.nicolaspetras.splfantasy.model.scoring.output.FantasyTeamScores
import com.nicolaspetras.splfantasy.model.output.FantasyTeam
import com.nicolaspetras.splfantasy.model.scoring.internal.SplMatchScore
import com.nicolaspetras.splfantasy.model.scoring.internal.SplPlayerMatchScore
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
    fantasyTeamScores: ArrayList<FantasyTeamScores>
): ArrayList<FantasyTeam> {
    val fantasyTeamList = arrayListOf<FantasyTeam>()
    for (score in fantasyTeamScores) {
        val fantasyTeam = FantasyTeam(
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
            totalTeamScore = score.overallTeamScore.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        )
        fantasyTeamList.add(fantasyTeam)
    }
    fantasyTeamList.sortBy { it.totalTeamScore }
    fantasyTeamList.reverse()
    return fantasyTeamList
}

/**
 * Returns the list off all fantasy team groups and the
 */
fun covertFantasyTeamScoresToGroupScoresForApi(
    fantasyTeamScores: ArrayList<FantasyTeamScores>
): ArrayList<FantasyGroup> {
    val allTeamScores = convertFantasyTeamScoresToOverallScoresForApi(fantasyTeamScores)
    val existingGroups = arrayListOf<String>()
    val groupsScores = arrayListOf<FantasyGroup>()

    for (teamScores in allTeamScores) {
        if (existingGroups.contains(teamScores.fantasyTeamGroup)) {
            val relevantGroup = groupsScores.find { it.name == teamScores.fantasyTeamGroup }
            relevantGroup?.fantasyTeams?.add(teamScores)
        } else {
            groupsScores.add(
                FantasyGroup(
                    teamScores.fantasyTeamGroup,
                    arrayListOf(teamScores)
                )
            )
            existingGroups.add(teamScores.fantasyTeamGroup)
        }
    }
    groupsScores.sortBy { it.name }
    return groupsScores
}
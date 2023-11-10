package com.nicolaspetras.splfantasy.service

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.output.FantasyGroup
import com.nicolaspetras.splfantasy.model.output.FantasyTeam
import com.nicolaspetras.splfantasy.model.scoring.output.FantasyTeamScores
import com.nicolaspetras.splfantasy.service.scorer.MatchScorer
import com.nicolaspetras.splfantasy.service.scorer.combineScoresForEachPlayer
import com.nicolaspetras.splfantasy.service.scorer.FantasyTeamScorer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SplFantasyManager(
    val matchScorer: MatchScorer,
    val fantasyTeamScorer: FantasyTeamScorer
) {
    private val stats = scrapeSplStats()
    private val scores = matchScorer.scoreMatches(stats)
    private val convertedScores = convertMatchScoresToPlayerScore(scores)
    private val seasonScores = combineScoresForEachPlayer(convertedScores)

    private val log: Logger = LoggerFactory.getLogger(javaClass)


    init {
        fantasyTeamScorer.scoreFantasyTeams(seasonScores)
        printSeasonScores(seasonScores)
    }

    fun getAllFantasyTeamsApiData(): ArrayList<FantasyTeam> {
        return convertFantasyTeamScoresToOverallScoresForApi(fantasyTeamScorer.fantasyTeamScores)
    }

    fun getGroupsApiData(): ArrayList<FantasyGroup> {
        return covertFantasyTeamScoresToGroupScoresForApi(fantasyTeamScorer.fantasyTeamScores)
    }

    fun getGroupsApiData2(): ArrayList<ArrayList<FantasyTeamScores>> {
        val fantasyTeamGroupScores: ArrayList<ArrayList<FantasyTeamScores>> = arrayListOf()
        // sort FantasyScores
        fantasyTeamScorer.fantasyTeamScores.sortWith(compareBy({it.group}, {it.overallTeamScore}))
        // separate each group into ArrayList
        val teamsInGroup = arrayListOf<FantasyTeamScores>()
        var currentGroupName = fantasyTeamScorer.fantasyTeamScores[0].group
        val existingGroups = arrayListOf<FantasyTeamGroupName>()
        for (fantasyTeam in fantasyTeamScorer.fantasyTeamScores) {

            if (currentGroupName != fantasyTeam.group) {
                // add group to output, and move on to next group
                existingGroups.add(currentGroupName)
                currentGroupName = fantasyTeam.group
                fantasyTeamGroupScores.add(teamsInGroup)
                teamsInGroup.clear()

                if (currentGroupName in existingGroups) {
                    log.error("Duplicate group")
                }
            }
            teamsInGroup.add(fantasyTeam)
        }
        return fantasyTeamGroupScores
    }
}


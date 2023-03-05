package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.model.api.FantasyGroupApiData
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.combineScoresForEachPlayer
import com.nicolaspetras.splfantasy.service.scorer.FantasyTeamScorer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import com.nicolaspetras.splfantasy.utility.convertFantasyTeamScoresToOverallScoresForApi
import com.nicolaspetras.splfantasy.utility.convertMatchScoresToPlayerScore
import com.nicolaspetras.splfantasy.utility.covertFantasyTeamScoresToGroupScoresForApi
import com.nicolaspetras.splfantasy.utility.printSeasonScores


class SplFantasyManager(
    val scorer: Scorer,
    val fantasyTeamScorer: FantasyTeamScorer
) {
    private val stats = scrapeSplStats()
    private val scores = scorer.scoreMatches(stats)
    private val convertedScores = convertMatchScoresToPlayerScore(scores)
    private val seasonScores = combineScoresForEachPlayer(convertedScores)

    init {
        fantasyTeamScorer.scoreFantasyTeams(seasonScores)
        printSeasonScores(seasonScores)
    }

    fun getAllFantasyTeamsApiData(): ArrayList<FantasyTeamApiData> {
        return convertFantasyTeamScoresToOverallScoresForApi(fantasyTeamScorer.fantasyTeamScores)
    }

    fun getGroupsApiData(): ArrayList<FantasyGroupApiData> {
        return covertFantasyTeamScoresToGroupScoresForApi(fantasyTeamScorer.fantasyTeamScores)
    }
}


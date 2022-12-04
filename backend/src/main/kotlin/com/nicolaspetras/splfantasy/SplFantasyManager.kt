package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.api.SplFantasyApi
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.combineScoresForEachPlayer
import com.nicolaspetras.splfantasy.service.scorer.teams.FantasyTeamScorer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import com.nicolaspetras.splfantasy.utility.convertFantasyTeamScoresToApiData
import com.nicolaspetras.splfantasy.utility.convertMatchScoresToPlayerScore
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController


class SplFantasyManager(
    val scorer: Scorer,
    val fantasyTeamScorer: FantasyTeamScorer
) {
    val stats = scrapeSplStats()
    val scores = scorer.scoreMatches(stats)
    val convertedScores = convertMatchScoresToPlayerScore(scores)
    val seasonScores = combineScoresForEachPlayer(convertedScores)

    init {
        fantasyTeamScorer.scoreFantasyTeams(seasonScores)
    }

    fun getFantasyApiData(): ArrayList<FantasyTeamApiData> {
        return convertFantasyTeamScoresToApiData(fantasyTeamScorer.fantasyTeamScores)
    }
}


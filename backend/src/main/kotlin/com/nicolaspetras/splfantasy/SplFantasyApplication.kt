package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.model.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplPlayer
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.teams.FantasyTeamScorer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplFantasyApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyApplication>(*args)
//    printSeasonScores(seasonScores)
//    printMatchScoresList(scores)
}

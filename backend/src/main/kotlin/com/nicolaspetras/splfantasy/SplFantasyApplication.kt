package com.nicolaspetras.splfantasy

import com.nicolaspetras.splfantasy.model.score.SplMatchScore
import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.combineScoresForEachPlayer
import com.nicolaspetras.splfantasy.service.scraper.scrapeSplStats
import com.nicolaspetras.splfantasy.utility.convertMatchScoresToPlayerScore
import com.nicolaspetras.splfantasy.utility.printMatchScores
import com.nicolaspetras.splfantasy.utility.printMatchScoresList
import com.nicolaspetras.splfantasy.utility.printSeasonScores
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.math.RoundingMode

@SpringBootApplication
class SplFantasyApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyApplication>(*args)
    val scorer = Scorer()
    val stats = scrapeSplStats()
    val scores = scorer.scoreMatches(stats)
    val convertedScores = convertMatchScoresToPlayerScore(scores)
    val seasonScores = combineScoresForEachPlayer(convertedScores)
    printSeasonScores(seasonScores)
//    printMatchScoresList(scores)
}


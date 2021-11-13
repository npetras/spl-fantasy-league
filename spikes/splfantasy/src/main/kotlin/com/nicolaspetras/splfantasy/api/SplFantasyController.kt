package com.nicolaspetras.splfantasy.api

import com.nicolaspetras.splfantasy.model.scoring.SplMatchScore
import com.nicolaspetras.splfantasy.scraper.scrapeWebsiteAndScoreStats
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SplFantasyController {

    val matchScoresWeek = scrapeWebsiteAndScoreStats()

    @GetMapping("/weekMatchScores")
    fun weekMatchScores(): ArrayList<SplMatchScore> {
        // return the match's stats
        return matchScoresWeek
    }
}
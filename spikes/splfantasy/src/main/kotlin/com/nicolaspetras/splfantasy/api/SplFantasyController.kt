package com.nicolaspetras.splfantasy.api

import com.nicolaspetras.splfantasy.model.scoring.SplMatchScore
import com.nicolaspetras.splfantasy.scraper.scrapeWebsiteAndScoreStats
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@RestController
@CrossOrigin(origins = ["*"])
class SplFantasyController {

    val matchScoresWeek = scrapeWebsiteAndScoreStats()

    @GetMapping("/weekMatchScores")
    fun weekMatchScores(): ArrayList<SplMatchScore> {
        // return the match's stats
        return matchScoresWeek
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/*").allowedOrigins("http://localhost:8080")
            }
        }
    }
}
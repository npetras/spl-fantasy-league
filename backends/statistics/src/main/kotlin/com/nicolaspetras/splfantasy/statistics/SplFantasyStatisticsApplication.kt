package com.nicolaspetras.splfantasy.statistics

import com.nicolaspetras.splfantasy.statistics.model.SplMatchStats
import com.nicolaspetras.splfantasy.statistics.model.SplTeamName
import com.nicolaspetras.splfantasy.statistics.model.`interface`.SplMatchStatsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class SplFantasyWebScraperApplication : CommandLineRunner {

    @Autowired
    lateinit var repository: SplMatchStatsRepository

    override fun run(vararg args: String) {
        repository.save(
            SplMatchStats(
                date = "Saturday, 12th June",
                homeTeamName = SplTeamName.ONI,
                awayTeamName = SplTeamName.STYX,
                homeTeamScore = 3,
                awayTeamScore = 0,
                games = arrayListOf()
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<SplFantasyWebScraperApplication>(*args)
}

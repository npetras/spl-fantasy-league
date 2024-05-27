package com.nicolaspetras.splfantasy.statistics

import com.nicolaspetras.splfantasy.statistics.model.SplMatchStats
import com.nicolaspetras.splfantasy.statistics.model.SplTeamName
import com.nicolaspetras.splfantasy.statistics.model.`interface`.SplMatchStatsRepository
import com.nicolaspetras.splfantasy.statistics.utilities.convertStringToLocalDate
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
        // try catch -- if date fails just input the original date string
        // need to add error processing for an empty date, invalid date (exception), etc.
        val date = convertStringToLocalDate("Saturday, 12th June")

        repository.save(
            SplMatchStats(
                date = date,
                originalDate = "Saturday, 12th June",
                splSplitOrTournament = "Road to Worlds",
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

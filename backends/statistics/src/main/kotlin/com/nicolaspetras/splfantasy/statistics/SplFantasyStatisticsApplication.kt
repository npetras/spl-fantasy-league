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
open class SplFantasyStatisticsApplication

fun main(args: Array<String>) {
    runApplication<SplFantasyStatisticsApplication>(*args)
}

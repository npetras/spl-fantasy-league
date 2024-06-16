package com.nicolaspetras.splfantasy.statistics.model.`interface`

import com.nicolaspetras.splfantasy.statistics.model.SplMatchStats
import com.nicolaspetras.splfantasy.statistics.model.SplTeamName
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

//@Repository
// TODO: move to CoroutineCrudRepository in the future?
interface SplMatchStatsRepository: MongoRepository<SplMatchStats, String> {
      fun findByDate(date: LocalDate)
      fun findByDateAndHomeTeamNameAndAwayTeamName(date: LocalDate, homeTeamName: SplTeamName, awayTeamName: SplTeamName)
      @Query("{ 'date' : { \$gte: ?0, \$lte: ?1 } }")
      fun findByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<SplMatchStats>
}
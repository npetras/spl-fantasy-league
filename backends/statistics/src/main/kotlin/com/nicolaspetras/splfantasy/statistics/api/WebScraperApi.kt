package com.nicolaspetras.splfantasy.statistics.api

import com.nicolaspetras.splfantasy.statistics.model.SplMatchStats
import com.nicolaspetras.splfantasy.statistics.model.`interface`.SplMatchStatsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.*

@RestController
class WebScraperApi(
    @Autowired private val splMatchStatsRepository: SplMatchStatsRepository
) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * Returns the [SplMatchStats] for a specific timeframe.
     */
    @GetMapping("/match-stats/date-range")
    fun getMatchStatsForTimeframe(@RequestParam startDate: String, @RequestParam endDate: String): ResponseEntity<Any> {
        return try {
            val startDateObject = LocalDate.parse(startDate)
            val endDateObject = LocalDate.parse(endDate)
            val matchStats = splMatchStatsRepository.findByDateRange(
                startDate = startDateObject.atStartOfDay(),
                endDate = endDateObject.atTime(23, 59)
            )
            ResponseEntity.ok(matchStats)
        } catch (e: DateTimeException) {
            log.error("Input date range: from $startDate to $endDate could not be processed: ${e.message}")
            ResponseEntity.badRequest().body("Invalid date format: ${e.message}")
        } catch (e: IllegalArgumentException) {
            val errorMsg = "Invalid data provided: ${e.message}"
            log.error(errorMsg)
            ResponseEntity.badRequest().body(errorMsg)
        } catch (e: DataAccessException) {
            val errorMsg = "Database access error: ${e.message}"
            log.error(errorMsg)
            ResponseEntity.status(500).body(errorMsg)
        } catch (e: Exception) {
            val errorMsg = "An unexpected error occurred: ${e.message}"
            log.error(errorMsg)
            ResponseEntity.status(500).body(errorMsg)
        }
    }

    /**
     * Returns all the statistics in the current season
     */
    @GetMapping("/statistics/all-for-current-season")
    fun getAllStatisticsForSeason() {
        // return all the available data for the season
    }

    /**
     *
     */
    @GetMapping("/statistics/all-for-current-split")
    fun getAllStatisticsForSplit() {

    }
}
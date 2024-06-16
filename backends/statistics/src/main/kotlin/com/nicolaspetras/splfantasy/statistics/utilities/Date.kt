package com.nicolaspetras.splfantasy.statistics.utilities

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.DateTimeException
import java.time.LocalDate
import java.time.MonthDay
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

val log: Logger = LoggerFactory.getLogger("com.nicolaspetras.splfantasy.utilities.Date")


/**
 * Converts the string [dateString] scrapped from the Smite Pro League website to a [LocalDate] object.
 *
 * @throws DateTimeParseException if the parsing of the date string fails
 */
fun convertStringToLocalDate(dateString: String): LocalDate {
    try {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d['st']['nd']['rd']['th'] MMMM", Locale.ENGLISH)
        val monthDay = MonthDay.parse(dateString, formatter)
        val currentYear = LocalDate.now().year
        val date = monthDay.atYear(currentYear)
        log.debug("String date '{}' parsed to: '{}'", dateString, date)
        return date
    } catch (exception: DateTimeException) {
        log.error("Failed to parse date string: '$dateString', with error: ${exception.message}")
        log.info("Returning today's date: ${LocalDate.now()}")
        return LocalDate.now()
    }
}
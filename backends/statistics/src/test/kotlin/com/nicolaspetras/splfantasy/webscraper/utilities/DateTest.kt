package com.nicolaspetras.splfantasy.webscraper.utilities

import com.nicolaspetras.splfantasy.statistics.utilities.convertStringToLocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateTest {

    @Test
    fun `convert string date with 1 digit date to date object`() {
        val dateString = "Friday, 1st November"
        val currentYear = LocalDate.now().year
        val expectedDateObject = LocalDate.of(currentYear,11, 1)
        val actualDateObject = convertStringToLocalDate(dateString)
        assertEquals(expectedDateObject, actualDateObject)
    }

    @Test
    fun `convert string date with 2 digit date to date object`() {
        val dateString = "Saturday, 12th June"
        val currentYear = LocalDate.now().year
        val expectedDateObject = LocalDate.of(currentYear,6, 12)
        val actualDateObject = convertStringToLocalDate(dateString)
        assertEquals(expectedDateObject, actualDateObject)
    }

}
package com.nicolaspetras.splfantasy.webscraper

import com.nicolaspetras.splfantasy.statistics.api.WebScraperApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(WebScraperApi::class)
class WebScraperApiTest {

    @Autowired
    private lateinit var controller: WebScraperApi

    @Test
    fun contextLoads() {
        assertThat(controller).isNotNull()
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun dataSetup(): Unit {
        }
    }
}
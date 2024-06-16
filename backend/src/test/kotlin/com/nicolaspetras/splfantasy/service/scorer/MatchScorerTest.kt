package com.nicolaspetras.splfantasy.service.scorer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nicolaspetras.splfantasy.model.scoring.internal.SplMatchScore
import com.nicolaspetras.splfantasy.model.stat.collection.SplMatchStats
import com.nicolaspetras.splfantasy.service.printMatchScores
import com.nicolaspetras.splfantasy.service.scorer.rubric.OfficialRubricV1
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File

class MatchScorerTest {

    @Test
    fun `test styx oni warriors vs styx ferrymen - 1 game 1 match`() {
        val objectMapper = ObjectMapper()
        val json = File("src/test/kotlin/com/nicolaspetras/splfantasy/service/scorer/test_stats.json").readText()
        val matchStats = Json.decodeFromString<SplMatchStats>(json)
        val matchScorer = MatchScorer(OfficialRubricV1())
        val matchScores: ArrayList<SplMatchScore> = matchScorer.scoreMatches(arrayListOf(matchStats))
        print(matchScores)
    }
}
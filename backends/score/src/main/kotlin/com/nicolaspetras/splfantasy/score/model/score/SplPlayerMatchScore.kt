package com.nicolaspetras.splfantasy.score.model.score

import com.nicolaspetras.splfantasy.score.model.SplPlayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class SplPlayerMatchScore(
    val splPlayer: SplPlayer = SplPlayer(),
    var gameScores: Array<BigDecimal> = arrayOf(),
    var matchBonusPts: BigDecimal = BigDecimal.ZERO
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * Returns the overall match score for the player
     */
    fun overallMatchScore(): BigDecimal {
        if (gameScores.isNotEmpty()) {
            val cumlativeGameScore = gameScores.fold(BigDecimal.ZERO) { acc: BigDecimal, bigDecimal: BigDecimal -> acc + bigDecimal }
            return cumlativeGameScore + matchBonusPts
        } else {
            log.warn("Game scores are empty")
        }
        return BigDecimal.ZERO
    }
}
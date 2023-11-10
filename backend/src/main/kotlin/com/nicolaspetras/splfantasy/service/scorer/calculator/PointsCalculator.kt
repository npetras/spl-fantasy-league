package com.nicolaspetras.splfantasy.service.scorer.calculator

import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats

interface PointsCalculator {
    /**
     * Calculate the points
     */
    fun calculatePoints(playerStats: SplPlayerStats): Double
}
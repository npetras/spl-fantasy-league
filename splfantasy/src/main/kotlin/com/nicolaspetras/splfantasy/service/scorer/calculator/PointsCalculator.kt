package com.nicolaspetras.splfantasy.service.scorer.calculator

import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats

interface PointsCalculator {
    /**
     * Calculate the points
     */
    fun calculatePoints(playerStats: SplPlayerStats): Double
}
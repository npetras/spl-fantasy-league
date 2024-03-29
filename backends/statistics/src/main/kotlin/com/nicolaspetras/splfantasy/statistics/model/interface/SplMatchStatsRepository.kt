package com.nicolaspetras.splfantasy.statistics.model.`interface`

import com.nicolaspetras.splfantasy.statistics.model.SplMatchStats
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

//@Repository
interface SplMatchStatsRepository: MongoRepository<SplMatchStats, String> {
    fun findByDate(date: String)
}
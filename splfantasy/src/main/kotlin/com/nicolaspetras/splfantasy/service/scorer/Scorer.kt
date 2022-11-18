package com.nicolaspetras.splfantasy.service.scorer

import com.nicolaspetras.splfantasy.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.service.scorer.rubric.OfficialRubricV1
import com.nicolaspetras.splfantasy.service.scorer.rubric.Rubric

class Scorer(val rubric: Rubric = OfficialRubricV1()) {
    fun scoreMatch(): ArrayList<SplPlayerMatchScore> {

        return arrayListOf()
    }
}
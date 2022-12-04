package com.nicolaspetras.splfantasy.api

import com.nicolaspetras.splfantasy.SplFantasyManager
import com.nicolaspetras.splfantasy.model.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplPlayer
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.teams.FantasyTeamScorer
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SplFantasyApi() {

    final val scorer = Scorer()
    private final val fantasyTeamDrafts = arrayListOf<FantasyTeamPicks>(
        FantasyTeamPicks(
            "Xenico",
            SplPlayer("SoloOrTroll", SplTeamName.TITAN, SmiteRole.SOLO),
            SplPlayer("QvoFred", SplTeamName.TITAN, SmiteRole.JUNGLE),
            SplPlayer("Sheento", SplTeamName.LVTHN, SmiteRole.MID),
            SplPlayer("Ronngyu", SplTeamName.LVTHN, SmiteRole.SUPPORT),
            SplPlayer("Jarcorr", SplTeamName.KINGS, SmiteRole.HUNTER)
        ),
        FantasyTeamPicks(
            "Harrison",
            SplPlayer("Haddix", SplTeamName.LVTHN, SmiteRole.SOLO),
            SplPlayer("Scream", SplTeamName.JADE, SmiteRole.JUNGLE),
            SplPlayer("Paul", SplTeamName.TITAN, SmiteRole.MID),
            SplPlayer("Genetics", SplTeamName.KINGS, SmiteRole.SUPPORT),
            SplPlayer("Stuart", SplTeamName.SOLAR, SmiteRole.HUNTER)
        ),
        FantasyTeamPicks(
            "Lochie",
            SplPlayer("Nika", SplTeamName.ONI, SmiteRole.SOLO),
            SplPlayer("CaptainTwig", SplTeamName.KINGS, SmiteRole.JUNGLE),
            SplPlayer("Pegon", SplTeamName.JADE, SmiteRole.MID),
            SplPlayer("PolarBearMike", SplTeamName.JADE, SmiteRole.SUPPORT),
            SplPlayer("Cyclone", SplTeamName.TITAN, SmiteRole.HUNTER)
        )

    )
    private final val fantasyScorer = FantasyTeamScorer(fantasyTeamDrafts)
    val splFantasyManager = SplFantasyManager(scorer = scorer, fantasyTeamScorer = fantasyScorer)

    @CrossOrigin
    @GetMapping("/scores")
    fun getScores(): ArrayList<FantasyTeamApiData> {
        return splFantasyManager.getFantasyApiData()
    }
}
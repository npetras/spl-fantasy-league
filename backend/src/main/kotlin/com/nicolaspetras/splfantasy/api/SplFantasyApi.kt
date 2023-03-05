package com.nicolaspetras.splfantasy.api

import com.nicolaspetras.splfantasy.SplFantasyManager
import com.nicolaspetras.splfantasy.model.draft.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplPlayer
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.api.FantasyTeamApiData
import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.api.FantasyGroupApiData
import com.nicolaspetras.splfantasy.service.scorer.Scorer
import com.nicolaspetras.splfantasy.service.scorer.FantasyTeamScorer
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SplFantasyApi() {

    final val scorer = Scorer()
    private final val fantasyTeamDrafts = arrayListOf<FantasyTeamPicks>(
        FantasyTeamPicks(
            "Xenico",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.TITAN, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.TITAN, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.MID),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.AARU, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "KingUrk",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.VALKS, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.VALKS, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.AARU, SmiteRole.MID),
            SplPlayer("", SplTeamName.AARU, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Dawzy",
            FantasyTeamGroupName.BAKASURA,
            SplPlayer("", SplTeamName.TITAN, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.TITAN, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.MID),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.VALKS, SmiteRole.CARRY)
        )
    )

    private final val fantasyScorer = FantasyTeamScorer(fantasyTeamDrafts)

    val splFantasyManager = SplFantasyManager(scorer = scorer, fantasyTeamScorer = fantasyScorer)

    @CrossOrigin
    @GetMapping("/overallScores")
    fun getOverallScores(): ArrayList<FantasyTeamApiData> {
        return splFantasyManager.getAllFantasyTeamsApiData()
    }

    @CrossOrigin
    @GetMapping("/groupScores")
    fun getGroupScores(): ArrayList<FantasyGroupApiData> {
        return splFantasyManager.getGroupsApiData()
    }
}
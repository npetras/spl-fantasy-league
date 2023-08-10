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
            "KingUrk",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.ONI, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.MID),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.JADE, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Threadus",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.ONI, SmiteRole.MID),
            SplPlayer("", SplTeamName.JADE, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.ONI, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Lochie",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.JADE, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.JADE, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.STYX, SmiteRole.MID),
            SplPlayer("", SplTeamName.ONI, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Xenico",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.STYX, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.ONI, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.JADE, SmiteRole.MID),
            SplPlayer("", SplTeamName.STYX, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Coochieman",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.KINGS, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.RAVEN, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.RAVEN, SmiteRole.MID),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.CARRY)
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
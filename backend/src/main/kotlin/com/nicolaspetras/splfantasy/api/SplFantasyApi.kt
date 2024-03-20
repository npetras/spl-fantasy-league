package com.nicolaspetras.splfantasy.api

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import com.nicolaspetras.splfantasy.service.SplFantasyManager
import com.nicolaspetras.splfantasy.model.draft.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.SmiteRole
import com.nicolaspetras.splfantasy.model.SplPlayer
import com.nicolaspetras.splfantasy.model.SplTeamName
import com.nicolaspetras.splfantasy.model.output.FantasyTeam
import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.output.FantasyGroup
import com.nicolaspetras.splfantasy.service.scorer.MatchScorer
import com.nicolaspetras.splfantasy.service.scorer.FantasyTeamScorer

@RestController
class SplFantasyApi() {

    private final val matchScorer = MatchScorer()
    private final val fantasyTeamDrafts = arrayListOf<FantasyTeamPicks>(
        FantasyTeamPicks(
            "KingUrk",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.JADE, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.ONI, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.ONI, SmiteRole.MID),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.JADE, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Threadus",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.MID),
            SplPlayer("", SplTeamName.JADE, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Lochie",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.ONI, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.STYX, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.STYX, SmiteRole.MID),
            SplPlayer("", SplTeamName.ONI, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Xenico",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.STYX, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.HOUND, SmiteRole.MID),
            SplPlayer("", SplTeamName.STYX, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.ONI, SmiteRole.CARRY)
        ),
        FantasyTeamPicks(
            "Coochieman",
            FantasyTeamGroupName.AO_KUANG,
            SplPlayer("", SplTeamName.RAVEN, SmiteRole.SOLO),
            SplPlayer("", SplTeamName.JADE, SmiteRole.JUNGLE),
            SplPlayer("", SplTeamName.LVTHN, SmiteRole.MID),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.SUPPORT),
            SplPlayer("", SplTeamName.KINGS, SmiteRole.CARRY)
        )
    )

    private final val fantasyScorer = FantasyTeamScorer(fantasyTeamDrafts)

    val splFantasyManager = SplFantasyManager(matchScorer = matchScorer, fantasyTeamScorer = fantasyScorer)

    @CrossOrigin
    @GetMapping("/overallScores")
    fun getOverallScores(): ArrayList<FantasyTeam> {
        return splFantasyManager.getAllFantasyTeamsApiData()
    }

    @CrossOrigin
    @GetMapping("/groupScores")
    fun getGroupScores(): ArrayList<FantasyGroup> {
        return splFantasyManager.getGroupsApiData()
    }
}
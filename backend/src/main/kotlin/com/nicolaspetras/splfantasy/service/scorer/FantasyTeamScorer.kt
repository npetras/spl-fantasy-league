package com.nicolaspetras.splfantasy.service.scorer

import com.nicolaspetras.splfantasy.model.draft.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.scoring.output.FantasyTeamScores
import com.nicolaspetras.splfantasy.model.scoring.output.SplPlayerSeasonScore
import java.math.BigDecimal

class FantasyTeamScorer(
    private val fantasyTeamDrafts: ArrayList<FantasyTeamPicks>
) {
    val fantasyTeamScores: ArrayList<FantasyTeamScores> = arrayListOf()

    fun scoreFantasyTeams(playerSeasonScores: ArrayList<SplPlayerSeasonScore>) {
        for (fantasyTeam in fantasyTeamDrafts) {
            val soloSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.soloPick
            } ?: SplPlayerSeasonScore(fantasyTeam.soloPick, arrayListOf(BigDecimal("0.0")))
            val jungSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.junglePick
            } ?: SplPlayerSeasonScore(fantasyTeam.junglePick, arrayListOf(BigDecimal("0.0")))
            val midSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.midPick
            } ?: SplPlayerSeasonScore(fantasyTeam.midPick, arrayListOf(BigDecimal("0.0")))
            val suppSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.supportPick
            } ?: SplPlayerSeasonScore(fantasyTeam.supportPick, arrayListOf(BigDecimal("0.0")))
            val hunterSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.hunterPick
            } ?: SplPlayerSeasonScore(fantasyTeam.hunterPick, arrayListOf(BigDecimal("0.0")))
            fantasyTeamScores.add(
                FantasyTeamScores(
                    playerName = fantasyTeam.playerName,
                    group = fantasyTeam.group,
                    solo = soloSeasonScore,
                    jungle = jungSeasonScore,
                    mid = midSeasonScore,
                    support = suppSeasonScore,
                    hunter = hunterSeasonScore
                )
            )
        }
    }
}
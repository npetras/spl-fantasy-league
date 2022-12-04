package com.nicolaspetras.splfantasy.service.scorer.teams

import com.nicolaspetras.splfantasy.model.FantasyTeamPicks
import com.nicolaspetras.splfantasy.model.SplFantasyTeamScores
import com.nicolaspetras.splfantasy.model.SplPlayerSeasonScore
import org.springframework.stereotype.Service

class FantasyTeamScorer(
    val fantasyTeamDrafts: ArrayList<FantasyTeamPicks>
) {
    val fantasyTeamScores: ArrayList<SplFantasyTeamScores> = arrayListOf()

    fun scoreFantasyTeams(playerSeasonScores: ArrayList<SplPlayerSeasonScore>) {
        for (fantasyTeam in fantasyTeamDrafts) {
            val soloSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.soloPick
            }
            val jungSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.junglePick
            }
            val midSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.midPick
            }
            val suppSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.supportPick
            }
            val hunterSeasonScore = playerSeasonScores.find {
                it.splPlayer == fantasyTeam.hunterPick
            }
            fantasyTeamScores.add(
                SplFantasyTeamScores(
                    playerName = fantasyTeam.playerName,
                    solo = soloSeasonScore!!,
                    jungle = jungSeasonScore!!,
                    mid = midSeasonScore!!,
                    support = suppSeasonScore!!,
                    hunter = hunterSeasonScore!!
                )
            )

        }
    }
}
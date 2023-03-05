package com.nicolaspetras.splfantasy.model.draft

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName
import com.nicolaspetras.splfantasy.model.SplPlayer

class FantasyTeamPicks(
    val playerName: String,
    val group: FantasyTeamGroupName,
    val soloPick: SplPlayer,
    val junglePick: SplPlayer,
    val midPick: SplPlayer,
    val supportPick: SplPlayer,
    val hunterPick: SplPlayer
) {
}
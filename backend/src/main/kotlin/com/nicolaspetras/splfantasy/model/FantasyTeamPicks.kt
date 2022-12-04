package com.nicolaspetras.splfantasy.model

class FantasyTeamPicks(
    val playerName: String,
    val soloPick: SplPlayer,
    val junglePick: SplPlayer,
    val midPick: SplPlayer,
    val supportPick: SplPlayer,
    val hunterPick: SplPlayer
) {
}
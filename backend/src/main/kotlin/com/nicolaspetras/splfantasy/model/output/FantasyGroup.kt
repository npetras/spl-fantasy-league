package com.nicolaspetras.splfantasy.model.output

/**
 * A fantasy group, which has a [name] includes a number of [fantasyTeams].
 */
data class FantasyGroup(
    val name: String,
    val fantasyTeams: ArrayList<FantasyTeam>
)

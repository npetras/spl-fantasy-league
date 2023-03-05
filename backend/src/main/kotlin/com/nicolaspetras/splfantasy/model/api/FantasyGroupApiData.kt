package com.nicolaspetras.splfantasy.model.api

import com.nicolaspetras.splfantasy.model.FantasyTeamGroupName

data class FantasyGroupApiData(
    val groupName: String,
    val fantasyTeams: ArrayList<FantasyTeamApiData>
)

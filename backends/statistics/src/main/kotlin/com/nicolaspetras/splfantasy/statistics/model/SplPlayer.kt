package com.nicolaspetras.splfantasy.statistics.model

class SplPlayer(
    val name: String = "",
    val team: SplTeamName = SplTeamName.NONE,
    val role: SmiteRole = SmiteRole.NONE
) {
    override fun equals(other: Any?): Boolean {
        return if (other is SplPlayer) {
            this.team == other.team && this.role == other.role
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + team.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }
}
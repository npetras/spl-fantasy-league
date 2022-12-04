package com.nicolaspetras.splfantasy.model

class SplPlayer(
    val name: String,
    val team: SplTeamName,
    val role: SmiteRole
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
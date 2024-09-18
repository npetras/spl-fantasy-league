package com.nicolaspetras.splfantasy.statistics.model

/**
 * Represents a player competing in the Smite Pro League (SPL) in a professional capacity.
 *
 * @param name the player's in-game name (IGN)
 * @param team the team the player is competing under
 * @param role the player's assigned role in the current context (game)
 */
data class SplPlayer(
    val name: String = "",
    val team: SplTeamName = SplTeamName.NONE,
    val role: SmiteRole = SmiteRole.NONE
) {

    /**
     * Player with different names are treated as equal for the purpose of the game, because during the drafting phase
     * [SplPlayer]s are drafted based on their [team] and [role] for the purpose of fairness and consistency when any
     * roster changes or role swaps occur.
     */
    override fun equals(other: Any?): Boolean {
        return if (other is SplPlayer) {
            this.team == other.team && this.role == other.role
        } else {
            false
        }
    }

    /**
     * HashCode is generated from the team and role, because that is what determines equality.
     * See [equals] for more information.
     */
    override fun hashCode(): Int {
        var result = team.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }
}
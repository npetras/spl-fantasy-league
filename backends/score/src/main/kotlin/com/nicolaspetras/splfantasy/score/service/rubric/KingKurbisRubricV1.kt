package com.nicolaspetras.splfantasy.score.service.rubric

import com.nicolaspetras.splfantasy.score.model.SmiteRole
import com.nicolaspetras.splfantasy.score.model.SplPlayer
import com.nicolaspetras.splfantasy.score.model.score.SplPlayerMatchScore
import com.nicolaspetras.splfantasy.score.model.statistics.SplGameStats
import com.nicolaspetras.splfantasy.score.model.statistics.SplPlayerStats
import com.nicolaspetras.splfantasy.score.service.points.PlayerPointsCalculator
import org.slf4j.LoggerFactory
import java.math.BigDecimal


// kills
const val SOLO_KILL_PTS = 2.0
const val CARRY_JUNG_KILL_PTS = 2.5
const val SUPP_KILL_PTS = 1.5

// deaths
const val DEATH_PTS = -1.0

// assists
const val SOLO_ASSIST_PTS = 0.5
const val JUNG_ASSIST_PTS = 0.2
const val CARRY_ASSIST_PTS = 0.1
const val SUPP_ASSIST_PTS = 0.75

// damage mitigated
const val STD_MITIGATED_DMG_PTS = 0.00005
const val SUPP_MITIGATED_DMG_PTS = 0.000075

// player damage
const val TANKS_PLAYER_DMG_PTS = 0.000075
const val CARRY_JUNG_PLAYER_DMG_PTS = 0.0001

// gold per min, wards, structure damage
const val GOLD_PER_MIN_PTS = 0.001
const val WARDS_PTS = 0.1
const val STRUCTURE_DMG_PTS = 0.00025

// deathless
const val SOLO_DEATHLESS = 2.0
const val CARRY_JUNG_DEATHLESS_PTS = 3.0
const val SUPP_DEATHLESS_PTS = 4.0

// healing
const val HEALING_PTS = 0.0001

// bonus points
const val TOP_ASSISTS_GAME = 1.0                 // game or team
const val TOP_DAMAGE_GAME = 1.0
const val TOP_KILLS_GAME = 1.0
const val TOP_KILLS_GAME_SOLO = 1.0

/**
 * Scoring Rubric used in the Smite Pro League Fantasy server from around 2021 to 2023 that was administered and run
 * by King Kurbis.
 */
class KingKurbisRubricV1 : Rubric() {

    private val log = LoggerFactory.getLogger(javaClass)

    private val carryCalculator = PlayerPointsCalculator(
        killPts = CARRY_JUNG_KILL_PTS,
        deathPts = DEATH_PTS,
        assistPts = CARRY_ASSIST_PTS,
        damageMitigationPts = STD_MITIGATED_DMG_PTS,
        playerDamagePts = CARRY_JUNG_PLAYER_DMG_PTS,
        goldPerMinPts = GOLD_PER_MIN_PTS,
        wardPts = WARDS_PTS,
        structureDamagePts = STRUCTURE_DMG_PTS,
        deathlessPts = CARRY_JUNG_DEATHLESS_PTS,
        healingPts = HEALING_PTS
    )

    override val soloCalculator: PlayerPointsCalculator
        get() = PlayerPointsCalculator(
            killPts = SOLO_KILL_PTS,
            deathPts = DEATH_PTS,
            assistPts = SOLO_ASSIST_PTS,
            damageMitigationPts = STD_MITIGATED_DMG_PTS,
            playerDamagePts = TANKS_PLAYER_DMG_PTS,
            goldPerMinPts = GOLD_PER_MIN_PTS,
            wardPts = WARDS_PTS,
            structureDamagePts = STRUCTURE_DMG_PTS,
            deathlessPts = SOLO_DEATHLESS,
            healingPts = HEALING_PTS
        )

    override val jungleCalculator: PlayerPointsCalculator
        get() = PlayerPointsCalculator(
            killPts = CARRY_JUNG_KILL_PTS,
            deathPts = DEATH_PTS,
            assistPts = JUNG_ASSIST_PTS,
            damageMitigationPts = STD_MITIGATED_DMG_PTS,
            playerDamagePts = CARRY_JUNG_PLAYER_DMG_PTS,
            goldPerMinPts = GOLD_PER_MIN_PTS,
            wardPts = WARDS_PTS,
            structureDamagePts = STRUCTURE_DMG_PTS,
            deathlessPts = CARRY_JUNG_DEATHLESS_PTS,
            healingPts = HEALING_PTS
        )

    override val midCalculator: PlayerPointsCalculator
        get() = carryCalculator

    override val supportCalculator: PlayerPointsCalculator
        get() = PlayerPointsCalculator(
            killPts = SUPP_KILL_PTS,
            deathPts = DEATH_PTS,
            assistPts = SUPP_ASSIST_PTS,
            damageMitigationPts = SUPP_MITIGATED_DMG_PTS,
            playerDamagePts = TANKS_PLAYER_DMG_PTS,
            goldPerMinPts = GOLD_PER_MIN_PTS,
            wardPts = WARDS_PTS,
            structureDamagePts = STRUCTURE_DMG_PTS,
            deathlessPts = SUPP_DEATHLESS_PTS,
            healingPts = HEALING_PTS
        )

    override val hunterCalculator: PlayerPointsCalculator
        get() = carryCalculator

    /**
     * Calculate (and award) all the bonus points for a single game
     */
    override fun calculateBonusPointsForGame(gameStats: SplGameStats, gameIndex: Int, homeTeamScores: ArrayList<SplPlayerMatchScore>, awayTeamScores: ArrayList<SplPlayerMatchScore>) {
        // should this be changed with an error else?
        val homeTeamStats = if (gameStats.orderTeamName == homeTeamScores.first().splPlayer.team) {
            gameStats.orderTeamPlayerStats
        } else {
            gameStats.chaosTeamPlayerStats
        }
        val awayTeamStats = if (gameStats.orderTeamName == awayTeamScores.first().splPlayer.team) {
            gameStats.orderTeamPlayerStats
        } else {
            gameStats.chaosTeamPlayerStats
        }

        // top assists on each team
        awardTopAssistsGame(homeTeamStats, homeTeamScores, gameIndex)
        awardTopAssistsGame(awayTeamStats, awayTeamScores, gameIndex)
        // top assists in the game
        awardTopAssistsGame(homeTeamStats.plus(awayTeamStats), homeTeamScores.plus(awayTeamScores), gameIndex)
        // top damage on each team
        awardTopDamageGame(homeTeamStats, homeTeamScores, gameIndex)
        awardTopDamageGame(awayTeamStats, awayTeamScores, gameIndex)
        // top kills in the game
        awardTopKillsGame(homeTeamStats.plus(awayTeamStats), homeTeamScores.plus(awayTeamScores), gameIndex)
    }

    /**
     * Award the [SplPlayer] with the most assists on their team (per game) or top assists in the overall game with
     * bonus points specified in [TOP_ASSISTS_GAME] as long as they are a Support player.
     * @param playerStats The stats for a particular game - can be one team only or both teams in the game
     * @param playerScores The player scores for the match that the [playerStats] relate to
     */
    fun awardTopAssistsGame(
        playerStats: List<SplPlayerStats>,
        playerScores: List<SplPlayerMatchScore>,
        gameIndex: Int
    ) {
        val playerWithTopAssistsOnTeam = playerStats.maxBy { it.assists }.splPlayer
        if (playerWithTopAssistsOnTeam.role == SmiteRole.SUPPORT) {
            val playerMatchScore = playerScores.find { it.splPlayer == playerWithTopAssistsOnTeam }
            if (playerMatchScore != null) {
                playerMatchScore.gameScores[gameIndex] += BigDecimal(TOP_ASSISTS_GAME.toString())
            } else {
                log.error("Player: $playerWithTopAssistsOnTeam not found in $playerScores when awarding bonus point for top assists on team as support")
            }
            log.debug("Awarded $playerWithTopAssistsOnTeam with bonus point for top assists Support")
        } else {
            log.debug("Could not award bonus assist points because player: $playerWithTopAssistsOnTeam is not a Support")
        }
    }

    fun awardTopDamageGame(
        playerStats: List<SplPlayerStats>,
        playerScores: List<SplPlayerMatchScore>,
        gameIndex: Int
    ) {
        val playerWithTopDamageGame = playerStats.maxBy { it.playerDamage }.splPlayer
        val playerMatchScore = playerScores.find { it.splPlayer == playerWithTopDamageGame }
        if (playerMatchScore != null) {
            playerMatchScore.gameScores[gameIndex] += BigDecimal(TOP_DAMAGE_GAME.toString())
        } else {
            log.error("Player: $playerWithTopDamageGame not found in $playerScores when awarding bonus point for top damage in game")
        }
    }

    fun awardTopKillsGame(
        playerStats: List<SplPlayerStats>,
        playerScores: List<SplPlayerMatchScore>,
        gameIndex: Int,
    ) {
        val playerWithTopKillsGame = playerStats.maxBy { it.kills }.splPlayer
        val playerMatchScore = playerScores.find { it.splPlayer == playerWithTopKillsGame }
        if (playerMatchScore != null) {
            playerMatchScore.gameScores[gameIndex] += BigDecimal(TOP_KILLS_GAME.toString())
            // solo laners with top kills get an extra bonus point
            if (playerMatchScore.splPlayer.role == SmiteRole.SOLO) {
                playerMatchScore.gameScores[gameIndex] += BigDecimal(TOP_KILLS_GAME_SOLO.toString())
            }
        } else {
            log.error("Player: $playerWithTopKillsGame not found in $playerScores when awarding bonus point for top kills in game")

        }
    }

}
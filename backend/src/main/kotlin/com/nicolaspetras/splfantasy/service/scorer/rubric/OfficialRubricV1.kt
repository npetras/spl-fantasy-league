package com.nicolaspetras.splfantasy.service.scorer.rubric

import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.service.scorer.calculator.player.PlayerPointsCalculator

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

/**
 * Scoring Rubric currently used in the
 */
class OfficialRubricV1 : Rubric() {

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

    override
    val hunterCalculator: PlayerPointsCalculator
        get() = carryCalculator

}
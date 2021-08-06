package scoring

import data.SmiteRole
import data.collection.SplMatch
import data.collection.SplPlayerStats
import data.extraction.SplPlayerMatchScore
import java.lang.Math.floor

// standard pts
const val CARRIES_KILL = 2.0
const val TANKS_KILL = 1.5
const val DEATH = -1.0
const val TANKS_ASSIST = 0.5

// bonus pts
// role specific
const val CARRIES_TOP_KILLS_GAME = 1.0
const val CARRIES_NO_DEATH = 3.0
const val SUPP_TOP_ASSISTS_GAME = 1.0
const val SUPP_TOP_ASSISTS_TEAM = 1.0
const val SOLO_NO_DEATHS = 2.0
const val SOLO_TOP_KILLS_GAME = 2.0
const val SUPP_NO_DEATHS = 4.0

// general bonus points
const val TOP_DAMAGE_TEAM = 1.0
const val PENTA_KILL = 10.0
const val SWEEP_VICTORY = 1.0
const val GOLD_FURY = 0.5
const val FIRE_GIANT = 1.0

// if a carry has more than the assists specified by the threshold they gain the specified bonus points
const val CARRIES_ASSIST_THRESHOLD = 10.0
const val CARRIES_ASSIST_THRESHOLD_PTS = 1.0

fun scoreMatch(splMatch: SplMatch) {
    // make Array List team based?
    val team1Scores = arrayListOf<SplPlayerMatchScore>()
    val team2Scores = arrayListOf<SplPlayerMatchScore>()

    // get score for independent stats
    for (game in splMatch.games) {
        // independent stats for winning team
        for (playerStats in game.winningTeamStats) {
            if (playerStats.splTeam == splMatch.team1) {
                team1Scores.add(calculateIndependentStats(playerStats))
            } else if (playerStats.splTeam == splMatch.team2) {
                team2Scores.add(calculateIndependentStats(playerStats))
            } else {
                System.err.println("Error")
            }
        }
        // independent stats for losing team
        for (playerStats in game.losingTeamStats) {
            if (playerStats.splTeam == splMatch.team1) {
                team1Scores.add(calculateIndependentStats(playerStats))
            } else if (playerStats.splTeam == splMatch.team2) {
                team2Scores.add(calculateIndependentStats(playerStats))
            } else {
                System.err.println("Error")
            }
        }
    }

    println(team1Scores)
    println(team2Scores)

    // get scores for game wide stats
    for ((gameNum, game) in splMatch.games.withIndex()) {
        // top damage for each team
        var topDamageWinningTeam = SplPlayerStats()
        for (playerStats in game.winningTeamStats) {
            if (playerStats.playerDamage > topDamageWinningTeam.playerDamage) {
                topDamageWinningTeam = playerStats
            } else if (playerStats.playerDamage < topDamageWinningTeam.playerDamage) {
                 continue
            } else {
                System.err.println("Two player damage numbers are equal to each other")
            }
        }

        if (topDamageWinningTeam.splTeam == splMatch.team1) {
            val playerTeam1TopDmg = team1Scores.find { it.name == topDamageWinningTeam.name }
            println(playerTeam1TopDmg)
            playerTeam1TopDmg?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
            println(gameNum)
            println(playerTeam1TopDmg)
        } else if (topDamageWinningTeam.splTeam == splMatch.team2) {
            val playerTeam2TopDmg = team1Scores.find { it.name == topDamageWinningTeam.name }
            println(playerTeam2TopDmg)
            playerTeam2TopDmg?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
            println(gameNum)
            println(playerTeam2TopDmg)
        } else {
            System.err.println("Error neither team matches")
        }

        for (playerStats in game.losingTeamStats) {

        }

    }

    // per team stats
    //      top damage on team
    //      support - top assists on team?
    // overall game stats
    //      support - top assists in the game?
    //      top kills - pts change depending on if it is Carry role or solo
    //                  supports get no points for top kills
    // sweep victory ?
    //      check the score and identify the players that need to gain the extra point on their score

}

fun findTopDamageOnTeam(team: ArrayList<SplPlayerStats>, splMatch: SplMatch, team1Scores: SplPlayerMatchScore) {
    var topDamagePlayer = SplPlayerStats()
    for (playerStats in team) {
        if (playerStats.playerDamage > topDamagePlayer.playerDamage) {
            topDamagePlayer = playerStats
        } else if (playerStats.playerDamage < topDamagePlayer.playerDamage) {
            continue
        } else {
            System.err.println("Two player damage numbers are equal to each other")
        }
    }

    if (topDamagePlayer.splTeam == splMatch.team1) {
        val playerTeam1TopDmg = team1Scores.find { it.name == topDamageWinningTeam.name }
        println(playerTeam1TopDmg)
        playerTeam1TopDmg?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
        println(gameNum)
        println(playerTeam1TopDmg)
    } else if (topDamagePlayer.splTeam == splMatch.team2) {
        val playerTeam2TopDmg = team1Scores.find { it.name == topDamageWinningTeam.name }
        println(playerTeam2TopDmg)
        playerTeam2TopDmg?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
        println(gameNum)
        println(playerTeam2TopDmg)
    } else {
        System.err.println("Error neither team matches")
    }
}

fun calculateIndependentStats(playerStats: SplPlayerStats): SplPlayerMatchScore {
    // make below code into a function
    val playerMatchScore = SplPlayerMatchScore(
        name = playerStats.name,
        role = playerStats.role,
        team = playerStats.splTeam
    )

    when (playerStats.role) {
        SmiteRole.HUNTER, SmiteRole.JUNGLE, SmiteRole.MID ->
            playerMatchScore.gameScores.add(calculateCarryIndepedentStats(playerStats))
        SmiteRole.SOLO, SmiteRole.SUPPORT ->
            playerMatchScore.gameScores.add(calculateTankIndepedentStats(playerStats))
        else ->
            System.err.println("Error, player does not fall into the two valid role types")
    }
    return playerMatchScore
}

fun calculateTankIndepedentStats(playerStats: SplPlayerStats): Double {
    val killPts = playerStats.kills * CARRIES_KILL
    println("${playerStats.name} Kill points: $killPts")
    val deathsPts = playerStats.deaths * DEATH
    val assistBonusPts = kotlin.math.floor(playerStats.assists / CARRIES_ASSIST_THRESHOLD)
    val noDeathsBonusPts = if (playerStats.deaths == 0) CARRIES_NO_DEATH else 0.0
    return (killPts + deathsPts + assistBonusPts + noDeathsBonusPts)
}

fun calculateCarryIndepedentStats(playerStats: SplPlayerStats): Double {
    val killPts = playerStats.kills * TANKS_KILL
    println("${playerStats.name} Kill points: $killPts")
    val deathPts = playerStats.deaths * DEATH
    val assistPts = playerStats.assists * TANKS_ASSIST

    val noDeathsBonusPts =
        if (playerStats.role == SmiteRole.SUPPORT && playerStats.deaths == 0) {
            4.0
        } else if (playerStats.role == SmiteRole.SOLO && playerStats.deaths == 0) {
            2.0
        } else {
            0.0
        }

    return (killPts + deathPts + assistPts + noDeathsBonusPts)
}

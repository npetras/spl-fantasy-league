package scoring

import data.SmiteRole
import data.SplTeamName
import data.collection.SplMatch
import data.collection.SplPlayerStats
import data.extraction.SplPlayerMatchScore

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
    // create SplPlayerMatchScore objects
    val homeTeamScores = createTeamScoresBase(
        winningTeamName = splMatch.games[0].orderTeamName,
        teamName = splMatch.homeTeam,
        winningTeamStats = splMatch.games[0].winningTeamStats,
        losingTeamStats = splMatch.games[0].losingTeamStats
    )

    val awayTeamScores = createTeamScoresBase(
        winningTeamName = splMatch.games[0].orderTeamName,
        teamName = splMatch.awayTeam,
        winningTeamStats = splMatch.games[0].winningTeamStats,
        losingTeamStats = splMatch.games[0].losingTeamStats
    )

    // get score for independent stats
    for (game in splMatch.games) {
        // independent stats for winning team
        for (playerStats in game.winningTeamStats) {
            calculateAndRecordIndependentPlayerStats(
                playerStats = playerStats,
                homeTeam = splMatch.homeTeam,
                awayTeam = splMatch.awayTeam,
                homeTeamScores = homeTeamScores,
                awayTeamScores = awayTeamScores
            )
        }
        // independent stats for losing team
        for (playerStats in game.losingTeamStats) {
            calculateAndRecordIndependentPlayerStats(
                playerStats = playerStats,
                homeTeam = splMatch.homeTeam,
                awayTeam = splMatch.awayTeam,
                homeTeamScores = homeTeamScores,
                awayTeamScores = awayTeamScores
            )
        }
    }

    println("Home Team Scores: ")
    println(homeTeamScores)
    println("Away Team Scores: ")
    println(awayTeamScores)

    // get scores for game wide stats
    // top damage score
    for ((gameNum, game) in splMatch.games.withIndex()) {
        val topDamageWinningTeam = findTopDamagePlayerTeam(game.winningTeamStats)
        recordTopDamagePlayerTeam(
            topDamagePlayerTeam = topDamageWinningTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

        val topDamageLosingTeam = findTopDamagePlayerTeam(game.losingTeamStats)
        recordTopDamagePlayerTeam(
            topDamagePlayerTeam = topDamageLosingTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

    }

    for ((gameNum, game) in splMatch.games.withIndex()) {
        val topAssistsWinningTeam = findAssistsPlayerTeam(game.winningTeamStats)
    }


    // per team stats
    //      top damage on team - DONE
    //      support - top assists on team?
    // overall game stats
    //      support - top assists in the game?
    //      top kills - pts change depending on if it is Carry role or solo
    //                  supports get no points for top kills
    // sweep victory ?
    //      check the score and identify the players that need to gain the extra point on their score

}

fun findAssistsPlayerTeam(teamStats: ArrayList<SplPlayerStats>): SplPlayerStats {
    var topAssistTeam = SplPlayerStats()
    for (playerStats in teamStats) {
        if (playerStats.assists > topAssistTeam.assists) {
            topAssistTeam = playerStats
        } else if (playerStats.assists < topAssistTeam.assists) {
            continue
        } else {
            // if support is equal to another player they get credit
            if (playerStats.role == SmiteRole.SUPPORT) {
                topAssistTeam = playerStats
            }
        }
    }
    println(topAssistTeam)
    return topAssistTeam
}

private fun recordTopDamagePlayerTeam(
    topDamagePlayerTeam: SplPlayerStats,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    when (topDamagePlayerTeam.splTeam) {
        homeTeam -> {
            val playerInHomeTeam = homeTeamScores.find { it.name == topDamagePlayerTeam.name }
            playerInHomeTeam?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == topDamagePlayerTeam.name }
            playerInAwayTeam?.gameScores?.set(gameNum, TOP_DAMAGE_TEAM)
        }
        else -> {
            System.err.println("Error")
        }
    }
}

private fun findTopDamagePlayerTeam(teamStats: ArrayList<SplPlayerStats>): SplPlayerStats {
    var topDamageTeam = SplPlayerStats()
    for (playerStats in teamStats) {
        if (playerStats.playerDamage > topDamageTeam.playerDamage) {
            topDamageTeam = playerStats
        } else if (playerStats.playerDamage < topDamageTeam.playerDamage) {
            continue
        } else {
            System.err.println("Two player damage numbers are equal to each other")
        }
    }
    println(topDamageTeam)
    return topDamageTeam
}

private fun calculateAndRecordIndependentPlayerStats(
    playerStats: SplPlayerStats,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>
) {
    val playerIndependentScore = calculateIndependentStats(playerStats)

    when (playerStats.splTeam) {
        homeTeam -> {
            val playerInHomeTeam = homeTeamScores.find { it.name == playerStats.name }
            playerInHomeTeam?.gameScores?.add(playerIndependentScore)
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == playerStats.name }
            playerInAwayTeam?.gameScores?.add(playerIndependentScore)
        }
        else -> {
            System.err.println("Error")
        }
    }
}

fun createSplMatchScore(playerStats: SplPlayerStats): SplPlayerMatchScore {
    return SplPlayerMatchScore(
        name = playerStats.name,
        role = playerStats.role,
        team = playerStats.splTeam
    )
}

fun createTeamScoresBase(
    winningTeamName: SplTeamName,
    teamName: SplTeamName,
    winningTeamStats: ArrayList<SplPlayerStats>,
    losingTeamStats: ArrayList<SplPlayerStats>
): ArrayList<SplPlayerMatchScore> {
    val teamScores = arrayListOf<SplPlayerMatchScore>()

    if (winningTeamName == teamName) {
        for (playerStats in winningTeamStats) {
            teamScores.add(createSplMatchScore(playerStats))
        }
    } else {
        for (playerStats in losingTeamStats) {
            teamScores.add(createSplMatchScore(playerStats))
        }
    }
    return teamScores
}

fun calculateIndependentStats(playerStats: SplPlayerStats): Double {
    // make below code into a function
    var independentGameScore = 0.0
    when (playerStats.role) {
        SmiteRole.HUNTER, SmiteRole.JUNGLE, SmiteRole.MID ->
            independentGameScore = calculateCarryIndepedentStats(playerStats)
        SmiteRole.SOLO, SmiteRole.SUPPORT ->
            independentGameScore = calculateTankIndepedentStats(playerStats)
        else ->
            System.err.println("Error, player does not fall into the two valid role types")
    }
    return independentGameScore
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

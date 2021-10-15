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
        winningTeamStats = splMatch.games[0].orderTeamStats,
        losingTeamStats = splMatch.games[0].chaosTeamStats
    )

    val awayTeamScores = createTeamScoresBase(
        winningTeamName = splMatch.games[0].orderTeamName,
        teamName = splMatch.awayTeam,
        winningTeamStats = splMatch.games[0].orderTeamStats,
        losingTeamStats = splMatch.games[0].chaosTeamStats
    )

    // get score for independent stats
    for (game in splMatch.games) {
        // independent stats for winning team
        for (playerStats in game.orderTeamStats) {
            calculateAndRecordIndependentPlayerStats(
                playerStats = playerStats,
                homeTeam = splMatch.homeTeam,
                awayTeam = splMatch.awayTeam,
                homeTeamScores = homeTeamScores,
                awayTeamScores = awayTeamScores
            )
        }
        // independent stats for losing team
        for (playerStats in game.chaosTeamStats) {
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
        val topDamageWinningTeam = findTopDamagePlayerTeam(game.orderTeamStats)
        recordTopDamagePlayerTeam(
            topDamagePlayerTeam = topDamageWinningTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

        val topDamageLosingTeam = findTopDamagePlayerTeam(game.chaosTeamStats)
        recordTopDamagePlayerTeam(
            topDamagePlayerTeam = topDamageLosingTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

    }

    // record the score for supports with top assists on their team
    for ((gameNum, game) in splMatch.games.withIndex()) {
        val topAssistsOrderTeam = findAssistsPlayer(game.orderTeamStats)
        recordTopAssistPlayerTeam(
            topAssistPlayerTeam = topAssistsOrderTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

        val topAssistsChaosTeam = findAssistsPlayer(game.chaosTeamStats)
        recordTopAssistPlayerTeam(
            topAssistPlayerTeam = topAssistsChaosTeam,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
    }

    // find and record the score for supports with top overall assists in the whole game
    for ((gameNum, game) in splMatch.games.withIndex()) {
        val topAssistsPlayerGame = findAssistsPlayer(
            (game.orderTeamStats + game.chaosTeamStats) as ArrayList<SplPlayerStats>
        )
        recordTopAssistPlayerGame(
            topAssistPlayerGame = topAssistsPlayerGame,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
    }

    // find and record the score for Player with the top kills in the game
    for ((gameNum, game) in splMatch.games.withIndex()) {
        val topKillsPlayerGame = findTopKillsPlayer(
            (game.orderTeamStats + game.chaosTeamStats) as ArrayList<SplPlayerStats>
        )
        recordTopKillsPlayerGame(
            topKillPlayerGame = topKillsPlayerGame,
            homeTeam = splMatch.homeTeam,
            awayTeam = splMatch.awayTeam,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
    }

    // calculate overall match score
    for (playerScore in homeTeamScores) {
        playerScore.calculateOverallMatchScore()
    }
    for (playerScore in awayTeamScores) {
        playerScore.calculateOverallMatchScore()
    }


    // record extra points for teams that win in 2-0 'sweep' fashion
    // done afterwards because it is a per match score not a per game score
    if (splMatch.homeTeamScore == 2 && splMatch.awayTeamScore == 0) {
        for (playerScore in homeTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    } else if (splMatch.homeTeamScore == 0 && splMatch.awayTeamScore == 2) {
        for (playerScore in awayTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    }


    println("${splMatch.homeTeam} Home Team Scores")
    for (playerScore in homeTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }

    println()
    println("${splMatch.awayTeam} Away Team Scores")
    for (playerScore in awayTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }
}

fun findTopKillsPlayer(teamStats: ArrayList<SplPlayerStats>): SplPlayerStats {
    var topKillsPlayer = SplPlayerStats()
    for (playerStats in teamStats) {
        if (playerStats.kills > topKillsPlayer.kills) {
            topKillsPlayer = playerStats
        } else if (playerStats.kills < topKillsPlayer.kills) {
            continue
        } else {
            continue
        }
    }
    println(topKillsPlayer)
    return topKillsPlayer
}

fun recordTopKillsPlayerGame(
    topKillPlayerGame: SplPlayerStats,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    when (topKillPlayerGame.splTeam) {
        homeTeam -> {
            val playerInHomeTeam = homeTeamScores.find { it.name == topKillPlayerGame.name }
            recordTopKillPtsBasedOnRole(playerInHomeTeam, gameNum)
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == topKillPlayerGame.name }
            recordTopKillPtsBasedOnRole(playerInAwayTeam, gameNum)
        }
        else -> {
            System.err.println("Error")
        }
    }
}

private fun recordTopKillPtsBasedOnRole(topKillsPlayer: SplPlayerMatchScore?, gameNum: Int) {
    when (topKillsPlayer?.role) {
        SmiteRole.JUNGLE, SmiteRole.MID, SmiteRole.HUNTER -> {
            topKillsPlayer.gameScores[gameNum] += CARRIES_TOP_KILLS_GAME
        }
        SmiteRole.SOLO -> {
            topKillsPlayer.gameScores[gameNum] += SOLO_TOP_KILLS_GAME
        }
        SmiteRole.SUPPORT -> {
            println("Support had top kills in the game")
        }
        else -> {
            System.err.println("Error")
        }
    }
}

fun recordTopAssistPlayerGame(
    topAssistPlayerGame: SplPlayerStats,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    when (topAssistPlayerGame.splTeam) {
        homeTeam -> {
            val playerInHomeTeam = homeTeamScores.find { it.name == topAssistPlayerGame.name }
            if (playerInHomeTeam?.role == SmiteRole.SUPPORT) {
                playerInHomeTeam.gameScores[gameNum] += SUPP_TOP_ASSISTS_GAME
            }
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == topAssistPlayerGame.name }
            if (playerInAwayTeam?.role == SmiteRole.SUPPORT) {
                playerInAwayTeam.gameScores[gameNum] += SUPP_TOP_ASSISTS_GAME
            }
        }
        else -> {
            System.err.println("Error")
        }
    }
}

fun recordTopAssistPlayerTeam(
    topAssistPlayerTeam: SplPlayerStats,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    when (topAssistPlayerTeam.splTeam) {
        homeTeam -> {
            val playerInHomeTeam = homeTeamScores.find { it.name == topAssistPlayerTeam.name }
            if (playerInHomeTeam?.role == SmiteRole.SUPPORT) {
                playerInHomeTeam.gameScores[gameNum] += SUPP_TOP_ASSISTS_TEAM
            }
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == topAssistPlayerTeam.name }
            if (playerInAwayTeam?.role == SmiteRole.SUPPORT) {
                playerInAwayTeam.gameScores[gameNum] += SUPP_TOP_ASSISTS_TEAM
            }
        }
        else -> {
            System.err.println("Error")
        }
    }
}

fun findAssistsPlayer(teamStats: ArrayList<SplPlayerStats>): SplPlayerStats {
    var topAssistPlayer = SplPlayerStats()
    for (playerStats in teamStats) {
        if (playerStats.assists > topAssistPlayer.assists) {
            topAssistPlayer = playerStats
        } else if (playerStats.assists < topAssistPlayer.assists) {
            continue
        } else {
            // if support is equal to another player they get credit
            if (playerStats.role == SmiteRole.SUPPORT) {
                topAssistPlayer = playerStats
            }
        }
    }
    println(topAssistPlayer)
    return topAssistPlayer
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
            playerInHomeTeam!!.gameScores[gameNum] += TOP_DAMAGE_TEAM
        }
        awayTeam -> {
            val playerInAwayTeam = awayTeamScores.find { it.name == topDamagePlayerTeam.name }
            playerInAwayTeam!!.gameScores[gameNum] += TOP_DAMAGE_TEAM
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
            independentGameScore = calculateCarryIndependentStats(playerStats)
        SmiteRole.SOLO, SmiteRole.SUPPORT ->
            independentGameScore = calculateTankIndependentStats(playerStats)
        else ->
            System.err.println("Error, player does not fall into the two valid role types")
    }
    return independentGameScore
}

fun calculateCarryIndependentStats(playerStats: SplPlayerStats): Double {
    val killPts = playerStats.kills * CARRIES_KILL
    val deathsPts = playerStats.deaths * DEATH
    val assistBonusPts = kotlin.math.floor(playerStats.assists / CARRIES_ASSIST_THRESHOLD)
    val noDeathsBonusPts = if (playerStats.deaths == 0) CARRIES_NO_DEATH else 0.0

    println("${playerStats.name} Kill points: $killPts")
    return (killPts + deathsPts + assistBonusPts + noDeathsBonusPts)
}

fun calculateTankIndependentStats(playerStats: SplPlayerStats): Double {
    val killPts = playerStats.kills * TANKS_KILL
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

    println("${playerStats.name} Kill points: $killPts")
    return (killPts + deathPts + assistPts + noDeathsBonusPts)
}

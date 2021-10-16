package scoring

import data.SmiteRole
import data.SplTeamName
import data.collection.SplMatchStats
import data.collection.SplPlayerStats
import data.extraction.SplMatchScore
import data.extraction.SplPlayerMatchScore
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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

val log: Logger = LoggerFactory.getLogger("Scorer")

fun scoreMatch(splMatchStats: SplMatchStats): SplMatchScore {
    // create SplPlayerMatchScore objects
    val homeTeamScores = createTeamScoresBase(
        winningTeamName = splMatchStats.games[0].orderTeamName,
        teamName = splMatchStats.homeTeamName,
        winningTeamStats = splMatchStats.games[0].orderTeamStats,
        losingTeamStats = splMatchStats.games[0].chaosTeamStats
    )

    val awayTeamScores = createTeamScoresBase(
        winningTeamName = splMatchStats.games[0].orderTeamName,
        teamName = splMatchStats.awayTeamName,
        winningTeamStats = splMatchStats.games[0].orderTeamStats,
        losingTeamStats = splMatchStats.games[0].chaosTeamStats
    )

    // get score for independent stats
    for (game in splMatchStats.games) {
        // independent stats for winning team
        for (playerStats in game.orderTeamStats) {
            calculateAndRecordIndependentPlayerStats(
                playerStats = playerStats,
                homeTeam = splMatchStats.homeTeamName,
                awayTeam = splMatchStats.awayTeamName,
                homeTeamScores = homeTeamScores,
                awayTeamScores = awayTeamScores
            )
        }
        // independent stats for losing team
        for (playerStats in game.chaosTeamStats) {
            calculateAndRecordIndependentPlayerStats(
                playerStats = playerStats,
                homeTeam = splMatchStats.homeTeamName,
                awayTeam = splMatchStats.awayTeamName,
                homeTeamScores = homeTeamScores,
                awayTeamScores = awayTeamScores
            )
        }
    }
    log.debug("Home Team Match Scores, without game and team-wide stats added: ")
    log.debug(homeTeamScores.toString())
    log.debug("Away Team Match Scores, without game and team-wide stats added: ")
    log.debug(awayTeamScores.toString())

    // get scores for game wide stats
    // top damage value, per team
    for ((gameNum, game) in splMatchStats.games.withIndex()) {
        val topDamageOrderTeam = findTopDamagePlayerTeam(game.orderTeamStats)

        recordTopDamagePlayersTeam(
            topDamagePlayerTeam = topDamageOrderTeam,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
        log.debug("Game Specific Stats, Game No ${gameNum + 1}: ")
        log.debug("Top Damage Player, Order Team (${game.orderTeamName}): ")
        log.debug(topDamageOrderTeam.toString())

        val topDamageChaosTeam = findTopDamagePlayerTeam(game.chaosTeamStats)
        recordTopDamagePlayersTeam(
            topDamagePlayerTeam = topDamageChaosTeam,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )

        log.debug("Top Damage Player, Chaos Team (${game.chaosTeamName}): ")
        log.debug(topDamageChaosTeam.toString())

    }

    // record the score for supports with top assists on their team
    for ((gameNum, game) in splMatchStats.games.withIndex()) {
        val topAssistsOrderTeam = findAssistsPlayers(game.orderTeamStats)
        recordTopAssistPlayers(
            topAssistPlayers = topAssistsOrderTeam,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
        log.debug("Top Assists, Order Team (${game.orderTeamName}): ")
        log.debug(topAssistsOrderTeam.toString())

        val topAssistsChaosTeam = findAssistsPlayers(game.chaosTeamStats)
        recordTopAssistPlayers(
            topAssistPlayers = topAssistsChaosTeam,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
        log.debug("Top Assists, Chaos Team (${game.chaosTeamName}): ")
        log.debug(topAssistsChaosTeam.toString())
    }

    // find and record the score for supports with top overall assists in the whole game
    for ((gameNum, game) in splMatchStats.games.withIndex()) {
        val topAssistsPlayersGame = findAssistsPlayers(
            (game.orderTeamStats + game.chaosTeamStats) as ArrayList<SplPlayerStats>
        )
        recordTopAssistPlayers(
            topAssistPlayers = topAssistsPlayersGame,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
        log.debug("Top Assist Player in the Game: ")
        log.debug(topAssistsPlayersGame.toString())
    }

    // find and record the score for Player with the top kills in the game
    for ((gameNum, game) in splMatchStats.games.withIndex()) {
        val topKillsPlayerGame = findTopKillsPlayers(
            (game.orderTeamStats + game.chaosTeamStats) as ArrayList<SplPlayerStats>
        )
        recordTopKillsPlayersGame(
            topKillPlayersGame = topKillsPlayerGame,
            homeTeam = splMatchStats.homeTeamName,
            awayTeam = splMatchStats.awayTeamName,
            homeTeamScores = homeTeamScores,
            awayTeamScores = awayTeamScores,
            gameNum = gameNum
        )
        log.debug("Top Kills Player in the Game: ")
        log.debug(topKillsPlayerGame.toString())
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
    if (splMatchStats.homeTeamScore == 2 && splMatchStats.awayTeamScore == 0) {
        for (playerScore in homeTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    } else if (splMatchStats.homeTeamScore == 0 && splMatchStats.awayTeamScore == 2) {
        for (playerScore in awayTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    }



    return SplMatchScore(
        homeTeamName = splMatchStats.homeTeamName,
        awayTeamName = splMatchStats.awayTeamName,
        homeTeamScores = homeTeamScores,
        awayTeamScores = awayTeamScores
    )
}

fun findTopKillsPlayers(teamStats: ArrayList<SplPlayerStats>): ArrayList<SplPlayerStats> {
    var topKillsPlayers = arrayListOf<SplPlayerStats>(SplPlayerStats())
    for (playerStats in teamStats) {
        if (playerStats.kills > topKillsPlayers[0].kills) {
            topKillsPlayers.clear()
            topKillsPlayers.add(playerStats)
        } else if (playerStats.kills < topKillsPlayers[0].kills) {
            continue
        } else {
            topKillsPlayers.add(playerStats)
        }
    }
    return topKillsPlayers
}

fun recordTopKillsPlayersGame(
    topKillPlayersGame: ArrayList<SplPlayerStats>,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    for (player in topKillPlayersGame) {
        if (player.kills > 0) {
            when (player.splTeam) {
                homeTeam -> {
                    val playerInHomeTeam = homeTeamScores.find { it.name == player.name }
                    recordTopKillPtsBasedOnRole(playerInHomeTeam, gameNum)
                }
                awayTeam -> {
                    val playerInAwayTeam = awayTeamScores.find { it.name == player.name }
                    recordTopKillPtsBasedOnRole(playerInAwayTeam, gameNum)
                }
                else -> {
                    System.err.println("Error")
                }
            }
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
            log.info("Support had top kills in the game")
        }
        else -> {
            System.err.println("Error")
        }
    }
}

fun recordTopAssistPlayers(
    topAssistPlayers: ArrayList<SplPlayerStats>,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    for (player in topAssistPlayers) {
        if (player.assists > 0 && player.role == SmiteRole.SUPPORT) {
            when (player.splTeam) {
                homeTeam -> {
                    val playerInHomeTeam = homeTeamScores.find { it.name == player.name }
                    playerInHomeTeam!!.gameScores[gameNum] += SUPP_TOP_ASSISTS_TEAM
                }
                awayTeam -> {

                    val playerInAwayTeam = awayTeamScores.find { it.name == player.name }
                    playerInAwayTeam!!.gameScores[gameNum] += SUPP_TOP_ASSISTS_TEAM
                }
                else -> {
                    log.error("A Top Assist Player has an unknown team name, not in this match")
                    log.error("Player's Team: ${player.splTeam}")
                    log.error("Player: $player")
                }
            }
        }
    }
}


/**
 * Returns the player(s)([SplPlayerStats]) with the top assists in the team for the game provided with [teamStats].
 */
fun findAssistsPlayers(teamStats: ArrayList<SplPlayerStats>): ArrayList<SplPlayerStats> {
    var topAssistPlayers = arrayListOf<SplPlayerStats>(SplPlayerStats())
    for (playerStats in teamStats) {
        if (playerStats.assists > topAssistPlayers[0].assists) {
            topAssistPlayers.clear()
            topAssistPlayers.add(playerStats)
        } else if (playerStats.assists < topAssistPlayers[0].assists) {
            continue
        } else {
            topAssistPlayers.add(playerStats)
        }
    }
    return topAssistPlayers
}

/**
 * Records the top damage player for each team, by adding the corresponding bonus points to the player's
 * total for that game.
 */
private fun recordTopDamagePlayersTeam(
    topDamagePlayerTeam: ArrayList<SplPlayerStats>,
    homeTeam: SplTeamName,
    awayTeam: SplTeamName,
    homeTeamScores: ArrayList<SplPlayerMatchScore>,
    awayTeamScores: ArrayList<SplPlayerMatchScore>,
    gameNum: Int
) {
    for (player in topDamagePlayerTeam) {
        if (player.playerDamage > 0) {
            when (player.splTeam) {
                homeTeam -> {
                    val playerInHomeTeam = homeTeamScores.find { it.name == player.name }
                    playerInHomeTeam!!.gameScores[gameNum] += TOP_DAMAGE_TEAM
                }
                awayTeam -> {
                    val playerInAwayTeam = awayTeamScores.find { it.name == player.name }
                    playerInAwayTeam!!.gameScores[gameNum] += TOP_DAMAGE_TEAM

                }
                else -> {
                    log.error("A Top Damage Player has an unknown team name, not in this match")
                    log.error("Player's Team: ${player.splTeam}")
                    log.error("Player: $player")
                }
            }
        }
    }
}

/**
 * Returns an [ArrayList] of the player(s) ([SplPlayerStats]) with the top damage within [teamStats].
 * Top damage player(s) on the team. It is extremely unlikely that two players on one team have the same damage.
 */
private fun findTopDamagePlayerTeam(teamStats: ArrayList<SplPlayerStats>): ArrayList<SplPlayerStats> {
    var topDamageTeam = arrayListOf<SplPlayerStats>(SplPlayerStats())
    for (playerStats in teamStats) {
        if (playerStats.playerDamage > topDamageTeam[0].playerDamage) {
            topDamageTeam.clear()            // clear all former top damage player(s) in ArrayList
            topDamageTeam.add(playerStats)
        } else if (playerStats.playerDamage < topDamageTeam[0].playerDamage) {
            continue
        } else {
            topDamageTeam.add(playerStats)
        }
    }
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
    return (killPts + deathPts + assistPts + noDeathsBonusPts)
}

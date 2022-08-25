package scoring

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import data.SmiteRole
import data.TeamName
import data.collection.SplMatchStats
import data.collection.SplPlayerStats
import data.scoring.MatchScore
import data.scoring.PlayerScore

// standard pts
const val CARRIES_KILL = 2.0
const val SOLO_KILL = 1.5
const val SUPP_KILLS = 1.0
const val DEATH = -1.0
const val CARRIES_ASSIST = 0.5
const val TANKS_ASSIST = 1

// bonus pts
// role specific
const val CARRIES_TOP_KILLS_GAME = 1.0
const val CARRIES_NO_DEATH = 3.0
const val SUPP_TOP_ASSISTS_GAME = 1.0
const val SUPP_TOP_ASSISTS_TEAM = 1.0
const val SOLO_NO_DEATHS = 3.0
const val SOLO_TOP_KILLS_GAME = 2.0
const val SUPP_NO_DEATHS = 4.0

// general bonus points
const val TOP_DAMAGE_TEAM = 1.0
const val PENTA_KILL = 10.0
const val SWEEP_VICTORY = 1.0
const val GAME_WIN = 1.0
const val GOLD_FURY = 0.5
const val FIRE_GIANT = 1.0

val log: Logger = LoggerFactory.getLogger("Scorer")

/**
 * Returns the scores for each player for each game and the overall match, all packaged in a [MatchScore] object.
 * The scores are derived from the [splMatchStats] that are provided.
 */
fun scoreMatch(splMatchStats: SplMatchStats): MatchScore {
    // create SplPlayerMatchScore objects
    val homeTeamScores = createTeamScoresBase(
        orderTeamName = splMatchStats.games[0].orderTeamName,
        teamName = splMatchStats.homeTeamName,
        orderTeamStats = splMatchStats.games[0].orderTeamStats,
        chaosTeamStats = splMatchStats.games[0].chaosTeamStats
    )

    val awayTeamScores = createTeamScoresBase(
        orderTeamName = splMatchStats.games[0].orderTeamName,
        teamName = splMatchStats.awayTeamName,
        orderTeamStats = splMatchStats.games[0].orderTeamStats,
        chaosTeamStats = splMatchStats.games[0].chaosTeamStats
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

    // give each player a point for a game victory
    for (playerScore in homeTeamScores) {
        playerScore.overallMatchScore += GAME_WIN * splMatchStats.homeTeamScore
    }

    for (playerScore in awayTeamScores) {
        playerScore.overallMatchScore += GAME_WIN * splMatchStats.awayTeamScore
    }

    // record extra points for teams that win in 2-0 'sweep' fashion
    // done afterwards because it is a per match score not a per game score
    if (splMatchStats.homeTeamScore >= 2 && splMatchStats.awayTeamScore == 0) {
        for (playerScore in homeTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    } else if (splMatchStats.homeTeamScore == 0 && splMatchStats.awayTeamScore >= 2) {
        for (playerScore in awayTeamScores) {
            playerScore.overallMatchScore += SWEEP_VICTORY
        }
    }

    return MatchScore(
        homeTeamName = splMatchStats.homeTeamName,
        awayTeamName = splMatchStats.awayTeamName,
        homeTeamScores = homeTeamScores,
        awayTeamScores = awayTeamScores
    )
}

/**
 * Returns a [PlayerScore] that is already populated with the name, role and team name of each player for the
 * [teamName] provided.
 *
 * @param orderTeamName The name of the order team for a specific game
 */
fun createTeamScoresBase(
    orderTeamName: TeamName,
    teamName: TeamName,
    orderTeamStats: ArrayList<SplPlayerStats>,
    chaosTeamStats: ArrayList<SplPlayerStats>
): ArrayList<PlayerScore> {
    val teamScores = arrayListOf<PlayerScore>()

    if (orderTeamName == teamName) {
        for (playerStats in orderTeamStats) {
            teamScores.add(createSplPlayerMatchScore(playerStats))
        }
    } else {
        for (playerStats in chaosTeamStats) {
            teamScores.add(createSplPlayerMatchScore(playerStats))
        }
    }
    return teamScores
}

/**
 * Returns an [PlayerScore] that is based on the [playerStats] provided.
 * The [PlayerScore] will have the same name, role and team as [playerStats].
 */
fun createSplPlayerMatchScore(playerStats: SplPlayerStats): PlayerScore {
    return PlayerScore(
        name = playerStats.name,
        role = playerStats.role,
        team = playerStats.splTeam
    )
}

/**
 * Returns the player ([SplPlayerStats]) with the top kills in the [teamStats] ArrayList
 */
private fun findTopKillsPlayers(teamStats: ArrayList<SplPlayerStats>): ArrayList<SplPlayerStats> {
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

/**
 * Awards points to the players with the top kills in one of the games, by finding them in
 * [homeTeamScores] or [awayTeamScores] and updating the respective team scores, either [homeTeamScores] or
 * [awayTeamScores]
 *
 * @param topKillPlayersGame The player(s) with the top kills in the game
 * @param homeTeam Home team name
 * @param awayTeam Away team name
 * @param homeTeamScores Home team scores
 * @param awayTeamScores Away team scores
 * @param gameNum The game number that is being considered -- the game in which the players got the top kills
 */
private fun recordTopKillsPlayersGame(
    topKillPlayersGame: ArrayList<SplPlayerStats>,
    homeTeam: TeamName,
    awayTeam: TeamName,
    homeTeamScores: ArrayList<PlayerScore>,
    awayTeamScores: ArrayList<PlayerScore>,
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

/**
 * Awards the [topKillsPlayer] in [gameNum] with the right amount of points based on their role
 */
private fun recordTopKillPtsBasedOnRole(topKillsPlayer: PlayerScore?, gameNum: Int) {
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
        SmiteRole.SUB -> {
            log.error("Sub is of unknown role, register them with a temporary role")
        }
        else -> {
            System.err.println("Error")
        }
    }
}

/**
 * Awards points to the players with the top assist in the game on their team or game-wide, by finding them in
 * [homeTeamScores] or [awayTeamScores] and updating the respective team scores, either [homeTeamScores] or
 * [awayTeamScores]
 *
 * @param topAssistPlayers The player(s) with the top assist on their team or in the game
 * @param homeTeam Home team name
 * @param awayTeam Away team name
 * @param homeTeamScores Home team scores
 * @param awayTeamScores Away team scores
 * @param gameNum The game number that is being considered -- the game in which the players got the top assists
 */
private fun recordTopAssistPlayers(
    topAssistPlayers: ArrayList<SplPlayerStats>,
    homeTeam: TeamName,
    awayTeam: TeamName,
    homeTeamScores: ArrayList<PlayerScore>,
    awayTeamScores: ArrayList<PlayerScore>,
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
 * Awards points to the players with the top damage on their team, by finding them in
 * [homeTeamScores] or [awayTeamScores] and updating the respective team scores, either [homeTeamScores] or
 * [awayTeamScores]
 *
 * @param topDamagePlayerTeam The player(s) with the top damage on their team
 * @param homeTeam Home team name
 * @param awayTeam Away team name
 * @param homeTeamScores Home team scores
 * @param awayTeamScores Away team scores
 * @param gameNum The game number that is being considered -- the game in which the players got the top damage on their
 * team
 */
private fun recordTopDamagePlayersTeam(
    topDamagePlayerTeam: ArrayList<SplPlayerStats>,
    homeTeam: TeamName,
    awayTeam: TeamName,
    homeTeamScores: ArrayList<PlayerScore>,
    awayTeamScores: ArrayList<PlayerScore>,
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
 * Top damage player(s) on the team.
 * It is extremely unlikely that two players on one team have the same damage.
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

/**
 * Calculates the and awards points to each player for their independent stats. The independent stats are the stats
 * for which points can be awarded without considering any other players stats in the game, like kills, deaths,
 * assists, no death bonuses, etc.
 *
 * @param playerStats The stats for the player who is being scored
 * @param homeTeam The home team in the match
 * @param awayTeam The away team in the match
 * @param homeTeamScores Home team scores
 * @param awayTeamScores Home team scores
 */
private fun calculateAndRecordIndependentPlayerStats(
    playerStats: SplPlayerStats,
    homeTeam: TeamName,
    awayTeam: TeamName,
    homeTeamScores: ArrayList<PlayerScore>,
    awayTeamScores: ArrayList<PlayerScore>
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

fun calculateIndependentStats(playerStats: SplPlayerStats): Double {
    // make below code into a function
    var independentGameScore = 0.0
    when (playerStats.role) {
        SmiteRole.HUNTER, SmiteRole.JUNGLE, SmiteRole.MID ->
            independentGameScore = calculateCarryIndependentStats(playerStats)
        SmiteRole.SOLO, SmiteRole.SUPPORT ->
            independentGameScore = calculateTankIndependentStats(playerStats)
        SmiteRole.SUB ->
            log.error("Sub is of unknown role, register them with a temporary role")
        else ->
            System.err.println("Error, player does not fall into the two valid role types")
    }
    return independentGameScore
}

fun calculateCarryIndependentStats(playerStats: SplPlayerStats): Double {
    val killPts = playerStats.kills * CARRIES_KILL
    val deathsPts = playerStats.deaths * DEATH
    val assistPts = playerStats.assists * CARRIES_ASSIST
    val noDeathsBonusPts = if (playerStats.deaths == 0) CARRIES_NO_DEATH else 0.0
    return (killPts + deathsPts + assistPts + noDeathsBonusPts)
}

fun calculateTankIndependentStats(playerStats: SplPlayerStats): Double {
    val killPts =
        if (playerStats.role == SmiteRole.SOLO) playerStats.kills * SOLO_KILL else playerStats.kills * SUPP_KILLS
    val deathPts = playerStats.deaths * DEATH
    val assistPts = playerStats.assists * TANKS_ASSIST

    val noDeathsBonusPts =
        if (playerStats.role == SmiteRole.SUPPORT && playerStats.deaths == 0) {
            SUPP_NO_DEATHS
        } else if (playerStats.role == SmiteRole.SOLO && playerStats.deaths == 0) {
            SOLO_NO_DEATHS
        } else {
            0.0
        }
    return (killPts + deathPts + assistPts + noDeathsBonusPts)
}


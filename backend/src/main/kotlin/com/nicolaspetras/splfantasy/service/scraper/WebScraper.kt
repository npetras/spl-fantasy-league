package com.nicolaspetras.splfantasy.service.scraper

import com.nicolaspetras.splfantasy.model.stat.collection.SplGameStats
import com.nicolaspetras.splfantasy.model.stat.collection.SplMatchStats
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import org.openqa.selenium.*
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Wait
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration


val log: Logger = LoggerFactory.getLogger("com.nicolaspetras.splfantasy.service.scraper.WebScraper")

const val SCHEDULE_XPATH = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div[1]/div[2]"
const val UNKNOWN = "UNKNOWN"


/**
 * Scrapes the stats from the Smite Pro website
 * Scraping is done from the Schedule page, and scrapes all the games that have available stats
 */
fun scrapeSplStats(): List<SplMatchStats> {
//    val options = FirefoxOptions()
//    options.addArguments("-headless");
    val webDriver = FirefoxDriver()
    webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5))

    try {
        // got to schedule page, which includes results for each match
        webDriver.get("https://www.smiteproleague.com/schedule")
        val actionProvider = Actions(webDriver)
        val js: JavascriptExecutor = webDriver

        // check for error conditions
        if (!acceptCookies(webDriver, actionProvider)) {
            log.error("No stats scraped - could not accept cookies")
            return arrayListOf()
        }

        val dayElements = webDriver.findElements(By.xpath("$SCHEDULE_XPATH/div"))
        log.debug("Found ${dayElements.size} days of matches")
        if (dayElements.isEmpty()) {
            log.error("No stats scraped - could not find any match days")
            return arrayListOf()
        }

        val playerMatchStats: ArrayList<SplMatchStats> = arrayListOf()

        for ((dayIndex, _) in dayElements.withIndex()) {
            val matches =
                webDriver.findElements(By.xpath("$SCHEDULE_XPATH/div[${dayIndex + 1}]/div[2]/div/div[2]/div"))
            val dateElement =
                webDriver.findElements(By.xpath("$SCHEDULE_XPATH/div[${dayIndex + 1}]/div[1]"))
            val date = if (dateElement.isNotEmpty()) dateElement[0].text else UNKNOWN
            log.info("Scraping Day ${dayIndex + 1} - $date")
            log.debug("Found ${matches.size} matches on Day ${dayIndex + 1}")
            for ((matchIndex, _) in matches.withIndex()) {
                val matchUpXpath =
                    "$SCHEDULE_XPATH/div[${dayIndex + 1}]/div[2]/div/div[2]/div[${matchIndex + 1}]/div[1]"
                val homeTeamElementSchedulePage =
                    webDriver.findElements(By.xpath("$matchUpXpath/div[1]"))
                val awayTeamElementSchedulePage =
                    webDriver.findElements(By.xpath("$matchUpXpath/div[3]"))
                val homeTeamSchedulePage =
                    if (homeTeamElementSchedulePage.isNotEmpty()) homeTeamElementSchedulePage[0].text else UNKNOWN
                val awayTeamSchedulePage =
                    if (awayTeamElementSchedulePage.isNotEmpty()) awayTeamElementSchedulePage[0].text else UNKNOWN

                log.info("Scraping Match ${matchIndex + 1} - $homeTeamSchedulePage vs $awayTeamSchedulePage")

                // button to open the match and game stats
                val matchLinkXpath =
                    "$SCHEDULE_XPATH/div[${dayIndex + 1}]/div[2]/div/div[2]/div[${matchIndex + 1}]/div[3]/a"

                if (openMatchStats(webDriver, actionProvider, js, matchLinkXpath)) {
                    scrapeMatch(webDriver, matchIndex, playerMatchStats, actionProvider, date)
                } else {
                    log.warn("Cannot open Match ${matchIndex + 1} - $homeTeamSchedulePage vs $awayTeamSchedulePage")
                    continue
                }
            }
        }
        return playerMatchStats

    } finally {
        webDriver.quit()
    }
}

private fun scrapeMatch(
    webDriver: FirefoxDriver,
    matchIndex: Int,
    playerMatchStats: ArrayList<SplMatchStats>,
    actionProvider: Actions,
    date: String
) {
    val homeTeamElementMatchPage =
        webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[1]/div/strong"))
    val awayTeamElementMatchPage =
        webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[3]/div/strong"))
    val homeTeamMatchPage =
        if (homeTeamElementMatchPage.isNotEmpty()) homeTeamElementMatchPage[0].text else UNKNOWN
    val awayTeamMatchPage =
        if (awayTeamElementMatchPage.isNotEmpty()) awayTeamElementMatchPage[0].text else UNKNOWN


    val noStatsForMatchXpath = "/html/body/div/div/div[1]/div/div[2]/h1"
    if (webDriver.findElements(By.xpath(noStatsForMatchXpath)).isNotEmpty()) {
        log.error("No stats for Match ${matchIndex + 1}")
        webDriver.navigate().back()
        return
    } else if (homeTeamMatchPage == UNKNOWN) {
        log.error("No Home Team name on the Match page")
    } else if (awayTeamMatchPage == UNKNOWN) {
        log.error("No Away Team name on the Match page")
    } else {
        log.debug("Scraping Match Stats")
        playerMatchStats.add(
            scrapeMatchStats(
                webDriver = webDriver,
                actionProvider = actionProvider,
                date = date,
                homeTeam = homeTeamMatchPage,
                awayTeam = awayTeamMatchPage
            )
        )
    }
    webDriver.navigate().back()
}

/**
 * Go to specific phase or tournament on the Schedule page, the default will usually be the latest phase/split/tournament
 * that has happened but if a specific phase should be scraped and scored then the [phaseTournamentXpath] should be
 * provided, and the web page will be changed to that phase/tournament/split using the existing [driver],
 * [actionProvider] and [jsExecutor] that is provided.
 */
private fun goToSpecificPhaseOrTournament(
    driver: WebDriver,
    phaseTournamentXpath: String,
    actionProvider: Actions,
    jsExecutor: JavascriptExecutor
) {
    val phase3Button = driver.findElement(By.xpath(phaseTournamentXpath))
    jsExecutor.executeScript("arguments[0].scrollIntoView();", phase3Button)
    actionProvider.clickAndHold(phase3Button).build().perform()
    actionProvider.release(phase3Button).build().perform()

}

/**
 * Scrape and store all stats of both teams for one match
 */
private fun scrapeMatchStats(
    webDriver: WebDriver,
    actionProvider: Actions,
    date: String,
    homeTeam: String,
    awayTeam: String,
): SplMatchStats {

    val homeTeamScoreElement =
        webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]"))
    val awayTeamScoreElement =
        webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[3]"))
    val homeTeamScore =
        if (homeTeamScoreElement.isNotEmpty()) {
            homeTeamScoreElement[0].text.toInt()
        } else {
            log.warn("Home Team Score is missing for $homeTeam, it will not be considered in the scoring calculation")
            0
        }
    val awayTeamScore =
        if (awayTeamScoreElement.isNotEmpty()) {
            awayTeamScoreElement[0].text.toInt()
        } else {
            log.warn("Away Team Score is missing for $awayTeam, it will not be considered in the scoring calculation")
            0
        }

    // TODO: if you can't find number of games from scores - try and check it from game tabs
    val numOfGames = homeTeamScore + awayTeamScore
    log.debug("No. of Games: $numOfGames. Score $homeTeam $homeTeamScore - $awayTeamScore $awayTeam")

    val matchGames = arrayListOf<SplGameStats>()

    for (gameNum in 1..numOfGames) {

        log.info("Scraping Game $gameNum Stats")

        log.debug("Scraping Order Team Stats")
        val orderTeamStatsHeaderElement =
            webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2"))
        val orderTeamText = if (orderTeamStatsHeaderElement.isNotEmpty()) orderTeamStatsHeaderElement[0].text.substringBefore(" ") else UNKNOWN

//        if (orderTeamText != UNKNOWN) {
            val orderTeamBasicStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div"
            val orderTeamBasicStats = scrapeBasicStats(
                driver = webDriver,
                xpath = orderTeamBasicStatsXPath,
                teamName = orderTeamText
            )

            val orderTeamAdditionalStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
            val orderTeamAdditionalStatsTable =
                webDriver.findElements(By.xpath(orderTeamAdditionalStatsXPath))
            val orderTeamCompleteStats = scrapeAdditionalStats(
                additionalStatsTable = orderTeamAdditionalStatsTable,
                teamGameStats = orderTeamBasicStats
            )
//        } else {
//            log.error("Order Team Name is missing cannot scrape Order Team Stats")
//        }

        log.debug("Scraping Chaos Team Stats")
        val chaosTeamElement =
            webDriver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2"))
        val chaosTeamText = if (chaosTeamElement.isNotEmpty()) chaosTeamElement[0].text.substringBefore(" ") else UNKNOWN

//        if (chaosTeamText != UNKNOWN) {
            val chaosTeamBasicStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
            val chaosTeamBasicStats = scrapeBasicStats(
                driver = webDriver,
                xpath = chaosTeamBasicStatsXPath,
                teamName = chaosTeamText
            )

            val chaosTeamAdditionalStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]/div/table/tbody/tr"
            val chaosTeamAdditionalStatsTable =
                webDriver.findElements(By.xpath(chaosTeamAdditionalStatsXPath))

            val chaosTeamCompleteStats = scrapeAdditionalStats(
                additionalStatsTable = chaosTeamAdditionalStatsTable,
                teamGameStats = chaosTeamBasicStats
            )
//        }

        val game = SplGameStats(
            orderTeamName = enumValueOf(orderTeamText),
            chaosTeamName = enumValueOf(chaosTeamText),
            orderTeamStats = orderTeamCompleteStats,
            chaosTeamStats = chaosTeamCompleteStats
        )
        log.debug(game.toString())
        matchGames.add(game)

        // navigate to next game
        if (gameNum < numOfGames) {
            val nextGameXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[1]/div[${gameNum + 1}]"
            val nextGameButton = webDriver.findElement(By.xpath(nextGameXPath))
            actionProvider.clickAndHold(nextGameButton).build().perform()
            actionProvider.release(nextGameButton).build().perform()
        }
    }

    return SplMatchStats(
        date = date,
        homeTeamName = enumValueOf(homeTeam),
        awayTeamName = enumValueOf(awayTeam),
        homeTeamScore = homeTeamScore,
        awayTeamScore = awayTeamScore,
        games = matchGames
    )
}

private fun scrapeAdditionalStats(
    additionalStatsTable: List<WebElement>,
    teamGameStats: ArrayList<SplPlayerStats>
): ArrayList<SplPlayerStats> {
    for ((i, player) in additionalStatsTable.withIndex()) {
        val name = player.findElement(By.className("name")).text.uppercase()
        val goldPerMin = player.findElement(By.className("gpm")).text.toInt()
        val playerDamage = player.findElement(By.className("damage")).text
        val playerDamageInt = playerDamage.replace(",", "").toInt()
        val mitigatedDamage = player.findElement(By.className("dmg_mtgtd")).text
        val mitigatedDamageInt = mitigatedDamage.replace(",", "").toInt()
        val structureDamage = player.findElement(By.className("strct_dmg")).text
        val structureDamageInt = structureDamage.replace(",", "").toInt()
        val healing = player.findElement(By.className("healing")).text
        val healingInt = healing.replace(",", "").toInt()
        val wards = player.findElement(By.className("wards")).text.toInt()

        val playerStats = teamGameStats[i]

        if (playerStats.name == name) {
            playerStats.goldPerMin = goldPerMin
            playerStats.playerDamage = playerDamageInt
            playerStats.mitigatedDamage = mitigatedDamageInt
            playerStats.structureDamage = structureDamageInt
            playerStats.healing = healingInt
            playerStats.wards = wards
        } else {
            log.warn("Player $i in Additional Stats and Basic Stats names don't match")
            log.info("Trying to find matching player in Basic Stats")
            val playerStatsRetry = teamGameStats.find { it.name == name }
            if (playerStatsRetry != null) {
                playerStatsRetry.goldPerMin = goldPerMin
                playerStatsRetry.playerDamage = playerDamageInt
                playerStatsRetry.mitigatedDamage = mitigatedDamageInt
                playerStatsRetry.structureDamage = structureDamageInt
                playerStatsRetry.healing = healingInt
                playerStatsRetry.wards = wards
            } else {
                log.error("Could not find player with name: $name in Basic Stats Player List")
            }
        }
        log.debug(playerStats.toString())
    }
    return teamGameStats
}

private fun scrapeBasicStats(
    driver: WebDriver,
    xpath: String,
    teamName: String
): ArrayList<SplPlayerStats> {

    val teamsGameStats = arrayListOf<SplPlayerStats>()
    var divNum = 10

    for (i in 1..5) {
        try {
            val name = driver.findElement(By.xpath("$xpath[$divNum]")).text
            // if less than 5 players are present the team totals will be reached early
            if (name == "Team Totals" && i < 5) {
                log.warn("There are only $i players listed for this $teamName's game. Instead of the expected 5")
                break
            }
            divNum += 1
            val role = driver.findElement(By.xpath("$xpath[$divNum]")).text
            divNum += 2
            val kills = driver.findElement(By.xpath("$xpath[$divNum]")).text
            divNum += 1
            val deaths = driver.findElement(By.xpath("$xpath[$divNum]")).text
            divNum += 1
            val assists = driver.findElement(By.xpath("$xpath[$divNum]")).text
            divNum += 4

            val playerStats = SplPlayerStats(
                name = name.uppercase(),
                splTeam = enumValueOf(teamName),
                role = enumValueOf(role),
                kills = kills.toInt(),
                deaths = deaths.toInt(),
                assists = assists.toInt()
            )
            log.debug(playerStats.toString())
            teamsGameStats.add(playerStats)
        } catch (missingElementInMatch: NoSuchElementException) {
            log.error("Could not scrape basic data for player number: $i")
            continue
        }
    }
    return teamsGameStats
}

private fun openMatchStats(
    driver: WebDriver,
    actionProvider: Actions,
    js: JavascriptExecutor,
    openMatchStatsButtonXPath: String
): Boolean {
    var result = true
    try {
        val matchStatsButton =
            driver.findElement(By.xpath(openMatchStatsButtonXPath))
        js.executeScript("arguments[0].scrollIntoView();", matchStatsButton)
        actionProvider.clickAndHold(matchStatsButton).build().perform()
        actionProvider.release(matchStatsButton).build().perform()

    } catch (noSuchElement: NoSuchElementException) {
        result = false
    }
    return result
}

private fun moveToPrevWeek(
    driver: WebDriver,
    actionProvider: Actions
) {
    val prevArrow = driver.findElement(By.cssSelector(".date-selector > div:nth-child(1)"))
    actionProvider.clickAndHold(prevArrow).build().perform()
    actionProvider.release(prevArrow).build().perform()
}

/**
 * Accept the cookies on launching the website, cannot proceed with scraping before these cookies are accepted
 */
private fun acceptCookies(
    driver: WebDriver,
    actionProvider: Actions
): Boolean {
    val wait: Wait<WebDriver> = WebDriverWait(driver, Duration.ofSeconds(2))
    val acceptCookiesButton = driver.findElements(By.cssSelector(".approve"))
    // needed in addition to implicit wait for Cookies Pop-up
    wait.until { acceptCookiesButton[0].isDisplayed }
    return if (acceptCookiesButton.isNotEmpty()) {
        actionProvider.clickAndHold(acceptCookiesButton[0]).build().perform()
        actionProvider.release(acceptCookiesButton[0]).build().perform()
        true
    } else {
        false
    }
}
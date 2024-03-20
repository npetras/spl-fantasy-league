package com.nicolaspetras.splfantasy.service.scraper

import com.nicolaspetras.splfantasy.model.stat.collection.SplGameStats
import com.nicolaspetras.splfantasy.model.stat.collection.SplMatchStats
import com.nicolaspetras.splfantasy.model.stat.collection.SplPlayerStats
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.interactions.Actions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration


val log: Logger = LoggerFactory.getLogger("WebScraper")

/**
 * Scrapes the stats from the Smite Pro website
 * Scraping is done from the Schedule page, and scrapes all the games that have available stats
 */
fun scrapeSplStats(): List<SplMatchStats> {
//    val options = FirefoxOptions()
//    options.addArguments("-headless");
    val driver = FirefoxDriver()
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

    try {
        // got to schedule page, which includes results for each match
        driver.get("https://www.smiteproleague.com/schedule")
        val actionProvider = Actions(driver)
        val js: JavascriptExecutor = driver

        // TODO: Check if I can I remove the line below with implicit waits?
        Thread.sleep(1000)              // delay allowing the cookies box to pop up
        acceptCookies(driver, actionProvider)

        val scheduleXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div[1]/div[2]"
        val dayElements = driver.findElements(By.xpath("$scheduleXpath/div"))

        val playerMatchStats: ArrayList<SplMatchStats> = arrayListOf()

        for ((dayIndex, _) in dayElements.withIndex()) {
            val matches = driver.findElements(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div[1]/div[2]/div[${dayIndex+1}]/div[2]/div/div[2]/div"))
            for ((matchIndex, _) in matches.withIndex()) {
                // button to open the match and game stats
                val matchLinkXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div[1]/div[2]/div[${dayIndex+1}]/div[2]/div/div[2]/div[${matchIndex+1}]/div[3]/a"
                if (openMatchStats(driver, actionProvider, js, matchLinkXpath)) {
                    try {
                        playerMatchStats.add(scrapeMatchStats("", driver, actionProvider))
                    } catch (missingElementInMatch: NoSuchElementException) {
                        // TODO: Try and grab the name of the teams playing & log the match name
                        log.error("Match $matchIndex: unexpected structure and was missing an element")
                    } catch (exception: Exception) {
                        log.error("Match $matchIndex: unknown error caught")
                    }
                    driver.navigate().back()
                } else {
                    log.error("Match $matchIndex: cannot open match link for match and game stats")
                    break
                }
            }
        }

        return playerMatchStats

    } finally {
        driver.quit()
    }
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
    dateText: String,
    driver: WebDriver,
    actionProvider: Actions
): SplMatchStats {
    // get the team name & match score
    val homeTeamName =
        driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[1]/div/strong"))
    val awayTeamName =
        driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[3]/div/strong"))
    val homeTeamScore =
        driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]"))
    val awayTeamScore =
        driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[3]"))
    val homeTeamNameText = homeTeamName.text
    val awayTeamNameText = awayTeamName.text
    val homeTeamScoreInt = homeTeamScore.text.toInt()
    val awayTeamScoreInt = awayTeamScore.text.toInt()

    log.info("Scraping $homeTeamNameText vs $awayTeamNameText")
    log.debug("Score: $homeTeamNameText $homeTeamScoreInt $awayTeamNameText $awayTeamScoreInt")

    val numOfGames = homeTeamScoreInt + awayTeamScoreInt
    log.debug("No. of Games: $numOfGames")

    val matchGames = arrayListOf<SplGameStats>()

    for (gameNum in 1..numOfGames) {

        log.debug("Scraping Game $gameNum Stats")

        log.debug("Scraping Order Team Stats")
        // Scrape stats for order team
        val orderTeamStatsHeader =
            driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2"))
        val orderTeamText = orderTeamStatsHeader.text.substringBefore(" ")
        val orderTeamBasicStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div"
        val orderTeamBasicStats = scrapeBasicStats(
            driver = driver,
            xpath = orderTeamBasicStatsXPath,
            teamName = orderTeamText
        )

        val orderTeamAdditionalStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
        val orderTeamAdditionalStatsTable = driver.findElements(By.xpath(orderTeamAdditionalStatsXPath))
        val orderTeamCompleteStats = scrapeAdditionalStats(
            additionalStatsTable = orderTeamAdditionalStatsTable,
            teamGameStats = orderTeamBasicStats
        )

        // scrape stats for chaos team
        log.debug("Scraping Chaos Team Stats")
        val chaosTeam =
            driver.findElement(By.xpath("""/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2"""))
        val chaosTeamText = chaosTeam.text.substringBefore(" ")
        val chaosTeamBasicStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
        val chaosTeamBasicStats = scrapeBasicStats(
            driver = driver,
            xpath = chaosTeamBasicStatsXPath,
            teamName = chaosTeamText
        )

        val chaosTeamAdditionalStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]/div/table/tbody/tr"
        val chaosTeamAdditionalStatsTable = driver.findElements(By.xpath(chaosTeamAdditionalStatsXPath))

        val chaosTeamCompleteStats = scrapeAdditionalStats(
            additionalStatsTable = chaosTeamAdditionalStatsTable,
            teamGameStats = chaosTeamBasicStats
        )

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
            val nextGameXPath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[1]/div[${gameNum + 1}]"
            val nextGameButton = driver.findElement(By.xpath(nextGameXPath))
            actionProvider.clickAndHold(nextGameButton).build().perform()
            actionProvider.release(nextGameButton).build().perform()
        }
    }

    return SplMatchStats(
        date = dateText,
        homeTeamName = enumValueOf(homeTeamNameText),
        awayTeamName = enumValueOf(awayTeamNameText),
        homeTeamScore = homeTeamScoreInt,
        awayTeamScore = awayTeamScoreInt,
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

private fun scrapeBasicStats(driver: WebDriver, xpath: String, teamName: String): ArrayList<SplPlayerStats> {

    val teamsGameStats = arrayListOf<SplPlayerStats>()
    var divNum = 10

    for (i in 1..5) {
        try {
            val name = driver.findElement(By.xpath("$xpath[$divNum]")).text
            // if less than 5 players are present the team totals will be reached early
            if (name == "Team Totals" && i < 5) {
                log.warn("There are only $i players listed for this $teamName's game. Instead of the expected 5")
                break;
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

private fun acceptCookies(
    driver: WebDriver,
    actionProvider: Actions
) {
    val acceptCookiesButton = driver.findElement(By.cssSelector(".approve"))
    actionProvider.clickAndHold(acceptCookiesButton).build().perform()
    actionProvider.release(acceptCookiesButton).build().perform()
}
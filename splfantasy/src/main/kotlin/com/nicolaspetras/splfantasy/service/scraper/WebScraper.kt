package com.nicolaspetras.splfantasy.service.scraper

import com.nicolaspetras.splfantasy.model.collection.SplGameStats
import com.nicolaspetras.splfantasy.model.collection.SplMatchStats
import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("WebScraper")

fun scrapeSplStats(): SplMatchStats {
    WebDriverManager.firefoxdriver().setup()
    val driver = FirefoxDriver()

    try {
        driver.get("https://www.smiteproleague.com/matches/3913")
        val actionProvider = Actions(driver)
        val js: JavascriptExecutor = driver

        // delay allowing the cookies box to pop up
        Thread.sleep(1000)
        // accept cookies
        acceptCookies(driver, actionProvider)

        // for every group of matches
//        val matchesGroupedByDayXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div"
//        val matchGroups = driver.findElements(By.xpath(matchesGroupedByDayXpath))
//        val noMatchGroups = matchGroups.size

        val scoresWrapperXPath = "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div"


        // if this week has no stats move to previous week
//        moveToPrevWeek(driver, actionProvider)

//        for (i in 1..noMatchGroups) {
//            val matchesInGroupXPath = "$scoresWrapperXPath/div[$i]/div"
//            val matchesInGroup = driver.findElements(By.xpath(matchesInGroupXPath))
//            val noOfMatches = matchesInGroup.size
//            // for all matches scrape the stats and score the games
//            for (j in 1..noOfMatches) {
//                val openMatchStatsButtonXPath = "$matchesInGroupXPath[$j]/div[10]/a"
//                openGameStats(driver, actionProvider, js, openMatchStatsButtonXPath)
//                // TODO: date text will be wrong for match groups after the first
//                val splMatchStats = scrapeMatchStats(dateText, driver, actionProvider)
//                log.debug("SPL Match Stats: ")
//                log.debug(splMatchStats.toString())
////                calculateAndPrintScores(splMatchStats)
//                // back out of match (set)
//                driver.navigate().back()
//                Thread.sleep(1000)
//            }
//        }
        val splMatchStats = scrapeMatchStats("", driver, actionProvider)
        log.debug("SPL Match Stats: ")
        print(splMatchStats.toString())
        return splMatchStats

//        openGameStats(driver, actionProvider, js,
//            "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[2]/div[1]/div[10]/a")
//        val splMatchStats = scrapeMatchStats(dateText, driver, actionProvider)
//        calculateAndPrintScores(splMatchStats)

    } finally {
        driver.quit()
    }
}


private fun scrapeMatchStats(
    dateText: String,
    driver: FirefoxDriver,
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

    println("$homeTeamNameText vs $awayTeamNameText")
    println("Score: $homeTeamNameText $homeTeamScoreInt $awayTeamNameText $awayTeamScoreInt")
    println()

    val numOfGames = homeTeamScoreInt + awayTeamScoreInt
    log.debug("No. of Games: $numOfGames")

    val matchGames = arrayListOf<SplGameStats>()

    for (gameNum in 1..numOfGames) {

        log.debug("Game $gameNum Stats: ")

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
        // get damage stats for order team
        val orderTeamAdditionalStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
        val orderTeamAdditionalStatsTable = driver.findElements(By.xpath(orderTeamAdditionalStatsXPath))

        val orderTeamCompleteStats = scrapeAdditionalStats(
            additionalStatsTable = orderTeamAdditionalStatsTable,
            teamGameStats = orderTeamBasicStats
        )

        // scrape stats for chaos team
        val chaosTeamBasicStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
        val chaosTeam =
            driver.findElement(By.xpath("""/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2"""))
        val chaosTeamText = chaosTeam.text.substringBefore(" ")

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
            System.err.println("Names do not match")
        }

        log.debug(playerStats.toString())
    }
    return teamGameStats
}

private fun scrapeBasicStats(driver: FirefoxDriver, xpath: String, teamName: String): ArrayList<SplPlayerStats> {

    val teamsGameStats = arrayListOf<SplPlayerStats>()
    var divNum = 10

    for (i in 1..5) {

        val name = driver.findElement(By.xpath("$xpath[$divNum]")).text
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
    }
    return teamsGameStats
}


private fun openGameStats(
    driver: FirefoxDriver,
    actionProvider: Actions,
    js: JavascriptExecutor,
    openMatchStatsButtonXPath: String
) {
    val matchStatsButton =
        driver.findElement(By.xpath(openMatchStatsButtonXPath))
    js.executeScript("arguments[0].scrollIntoView();", matchStatsButton)
    actionProvider.clickAndHold(matchStatsButton).build().perform()
    actionProvider.release(matchStatsButton).build().perform()
    Thread.sleep(2000)
}

private fun moveToPrevWeek(
    driver: FirefoxDriver,
    actionProvider: Actions
) {
    val prevArrow = driver.findElement(By.cssSelector(".date-selector > div:nth-child(1)"))
    actionProvider.clickAndHold(prevArrow).build().perform()
    actionProvider.release(prevArrow).build().perform()
    Thread.sleep(2000)
}

private fun acceptCookies(
    driver: FirefoxDriver,
    actionProvider: Actions
) {
    val acceptCookiesButton = driver.findElement(By.cssSelector(".approve"))
    actionProvider.clickAndHold(acceptCookiesButton).build().perform()
    actionProvider.release(acceptCookiesButton).build().perform()
}
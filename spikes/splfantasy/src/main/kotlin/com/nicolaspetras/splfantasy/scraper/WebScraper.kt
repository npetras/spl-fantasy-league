package com.nicolaspetras.splfantasy.scraper

import com.nicolaspetras.splfantasy.model.collection.SplGameStats
import com.nicolaspetras.splfantasy.model.collection.SplMatchStats
import com.nicolaspetras.splfantasy.model.collection.SplPlayerStats
import com.nicolaspetras.splfantasy.model.scoring.SplMatchScore
import com.nicolaspetras.splfantasy.scoring.scoreMatch
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import org.slf4j.Logger
import org.slf4j.LoggerFactory


val log: Logger = LoggerFactory.getLogger("Main")

fun scrapeWebsite(): ArrayList<SplMatchScore> {
fun scrapeWebsiteAndScoreStats(): ArrayList<SplMatchScore> {
    val driver = FirefoxDriver()
    var matchScoresWeek = arrayListOf<SplMatchScore>()

    try {
        driver.get("https://www.smiteproleague.com/scores")
        val actionProvider = Actions(driver)
        val js: JavascriptExecutor = driver

        // delay allowing the cookies box to pop up
        Thread.sleep(1000)
        // accept cookies
        acceptCookies(driver, actionProvider)

        // print the heading, weeks subheading and the date of the match
        val heading = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/h1")
        val weeksSubHeading = driver.findElementByXPath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[2]/p")
        val date = driver.findElementByCssSelector("div.c-MatchSummaryCard:nth-child(1) > h2:nth-child(1)")
        // stored in variable, so it can be used later on, even when it is no longer part of the DOM
        val dateText = date.text
        println(heading.text)
        println(weeksSubHeading.text)
        println(date.text)
        println()

        // for every group of matches
        val matchesGroupedByDayXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div"
        val matchGroups = driver.findElementsByXPath(matchesGroupedByDayXpath)
        val noMatchGroups = matchGroups.size

        val scoresWrapperXPath = "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div"


        // if this week has no stats move to previous week
        // does element exist & does it contain the text 'No Stats for this Week'
        // move to the previous week
//        moveToPrevWeek(driver, actionProvider)

        for (i in 1..noMatchGroups) {
            val matchesInGroupXPath = "$scoresWrapperXPath/div[$i]/div"
            val matchesInGroup = driver.findElementsByXPath(matchesInGroupXPath)
            val noOfMatches = matchesInGroup.size
            // for all matches scrape the stats and score the games
            for (j in 1..noOfMatches) {
                val openMatchStatsButtonXPath = "$matchesInGroupXPath[$j]/div[10]/a"
                openGameStats(driver, actionProvider, js, openMatchStatsButtonXPath)
                // TODO: date text will be wrong for match groups after the first
                val splMatchStats = scrapeMatchStats(dateText, driver, actionProvider)
                log.debug("SPL Match Stats: ")
                log.debug(splMatchStats.toString())
                matchScoresWeek = calculateAndPrintScores(splMatchStats)
                // back out of match (set)
                driver.navigate().back()
                Thread.sleep(1000)
            }
        }

//        openGameStats(driver, actionProvider, js,
//            "/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[2]/div[1]/div[10]/a")
//        val splMatchStats = scrapeMatchStats(dateText, driver, actionProvider)
//        calculateAndPrintScores(splMatchStats)

    } finally {
        driver.quit()
    }
    return matchScoresWeek
}


private fun calculateAndPrintScores(splMatchStats: SplMatchStats): ArrayList<SplMatchScore> {
    val matchScoresWeek = arrayListOf<SplMatchScore>()
    // score the match
    val matchScore = scoreMatch(splMatchStats)
    matchScoresWeek.add(matchScore)

    println("${matchScore.homeTeamName} Team Scores: ")
    for (playerScore in matchScore.homeTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }

    println()
    println("${matchScore.awayTeamName} Team Scores: ")
    for (playerScore in matchScore.awayTeamScores) {
        println("${playerScore.name} ${playerScore.gameScores} ${playerScore.overallMatchScore}")
    }
    println()

    return matchScoresWeek
}


private fun scrapeMatchStats(
    dateText: String,
    driver: FirefoxDriver,
    actionProvider: Actions
): SplMatchStats {
    // get the team name & match score
    val homeTeamName =
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[1]/div/strong")
    val awayTeamName =
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[3]/div/strong")
    val homeTeamScore =
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]")
    val awayTeamScore =
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[3]")
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
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2")
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
        val orderTeamAdditionalStatsTable = driver.findElementsByXPath(orderTeamAdditionalStatsXPath)

        val orderTeamCompleteStats = scrapeAdditionalStats(
            additionalStatsTable = orderTeamAdditionalStatsTable,
            teamGameStats = orderTeamBasicStats
        )

        // scrape stats for chaos team
        val chaosTeamBasicStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
        val chaosTeam =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2")
        val chaosTeamText = chaosTeam.text.substringBefore(" ")

        val chaosTeamBasicStats = scrapeBasicStats(
            driver = driver,
            xpath = chaosTeamBasicStatsXPath,
            teamName = chaosTeamText
        )

        val chaosTeamAdditionalStatsXPath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]/div/table/tbody/tr"
        val chaosTeamAdditionalStatsTable = driver.findElementsByXPath(chaosTeamAdditionalStatsXPath)

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
            val nextGameButton = driver.findElementByXPath(nextGameXPath)
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
        val playerDamage = player.findElement(By.className("damage")).text
        val playerDamageInt = playerDamage.replace(",", "").toInt()

        val playerStats = teamGameStats[i]

        if (playerStats.name == name) {
            playerStats.playerDamage = playerDamageInt
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

        val name = driver.findElementByXPath("$xpath[$divNum]").text
        divNum += 1
        val role = driver.findElementByXPath("$xpath[$divNum]").text
        divNum += 2
        val kills = driver.findElementByXPath("$xpath[$divNum]").text
        divNum += 1
        val deaths = driver.findElementByXPath("$xpath[$divNum]").text
        divNum += 1
        val assists = driver.findElementByXPath("$xpath[$divNum]").text
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
        driver.findElementByXPath(openMatchStatsButtonXPath)
    js.executeScript("arguments[0].scrollIntoView();", matchStatsButton);
    actionProvider.clickAndHold(matchStatsButton).build().perform()
    actionProvider.release(matchStatsButton).build().perform()
    Thread.sleep(2000)
}

private fun moveToPrevWeek(
    driver: FirefoxDriver,
    actionProvider: Actions
) {
    val prevArrow = driver.findElementByCssSelector(".date-selector > div:nth-child(1)")
    actionProvider.clickAndHold(prevArrow).build().perform()
    actionProvider.release(prevArrow).build().perform()
    Thread.sleep(2000)
}

private fun acceptCookies(
    driver: FirefoxDriver,
    actionProvider: Actions
) {
    val acceptCookiesButton = driver.findElementByCssSelector(".approve")
    actionProvider.clickAndHold(acceptCookiesButton).build().perform()
    actionProvider.release(acceptCookiesButton).build().perform()
}

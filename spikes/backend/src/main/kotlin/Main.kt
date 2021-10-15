import data.collection.SplGame
import data.collection.SplMatch
import data.collection.SplPlayerStats
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import org.slf4j.LoggerFactory
import scoring.scoreMatch

fun main() {
    val log = LoggerFactory.getLogger("Main")

    val driver = FirefoxDriver()
    lateinit var splMatch: SplMatch

    try {
        driver.get("https://www.smiteproleague.com/scores")
        val actionProvider = Actions(driver)

        // delay allowing the cookies box to pop up
        Thread.sleep(1000)
        // accept cookies
        acceptCookies(driver, actionProvider)
        // move to the previous week
        navigateToStats(driver, actionProvider)

        // print the heading, weeks subheading and the date of the match
        val heading = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/h1")
        val weeksSubHeading = driver.findElementByXPath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[2]/p")
        val date = driver.findElementByCssSelector("div.c-MatchSummaryCard:nth-child(1) > h2:nth-child(1)")
        // stored in variable, so it can be used later on, even when it is no longer part of the DOM
        val dateText = date.text
        println(heading.text)
        println(weeksSubHeading.text)
        println(date.text)

        // open the stats for the game
        openGameStats(driver, actionProvider)

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

        val numOfGames = homeTeamScoreInt + awayTeamScoreInt
        log.debug("No. of Games: $numOfGames")

        val matchGames = arrayListOf<SplGame>()

        for (gameNum in 1..numOfGames) {

            log.debug("#### Game $gameNum Stats ###")

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

            val game = SplGame(
                orderTeamName = enumValueOf(orderTeamText),
                chaosTeamName = enumValueOf(chaosTeamText),
                orderTeamStats = orderTeamCompleteStats,
                chaosTeamStats = chaosTeamCompleteStats
            )
            println(game)
            matchGames.add(game)

            // navigate to next game
            if (gameNum < numOfGames) {
                val nextGameXPath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[1]/div[${gameNum + 1}]"
                val nextGameButton = driver.findElementByXPath(nextGameXPath)
                actionProvider.clickAndHold(nextGameButton).build().perform()
                actionProvider.release(nextGameButton).build().perform()
            }

        }

        splMatch = SplMatch(
            date = dateText,
            homeTeam = enumValueOf(homeTeamNameText),
            awayTeam = enumValueOf(awayTeamNameText),
            homeTeamScore = homeTeamScoreInt,
            awayTeamScore = awayTeamScoreInt,
            games = matchGames
        )

        println("Spl Match Stats")
        println(splMatch)

    } finally {
        driver.quit()
    }

    // score the match
    scoreMatch(splMatch)
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

        println(playerStats)
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
        println(playerStats)
        teamsGameStats.add(playerStats)
    }
    println()
    return teamsGameStats
}


private fun openGameStats(
    driver: FirefoxDriver,
    actionProvider: Actions
) {
    val matchStats =
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[1]/div[1]/div[10]/a")
    actionProvider.clickAndHold(matchStats).build().perform()
    actionProvider.release(matchStats).build().perform()
    Thread.sleep(2000)
}

private fun navigateToStats(
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

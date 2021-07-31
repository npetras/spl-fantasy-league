import data.collection.SplGame
import data.collection.SplMatch
import data.collection.SplPlayerStats
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions

fun main() {
    val driver = FirefoxDriver()
    lateinit var splMatch: SplMatch

    try {
        driver.get("https://www.smiteproleague.com/scores")
        val actionProvider = Actions(driver)

        Thread.sleep(1000)
        // accept cookies
        acceptCookies(driver, actionProvider)
        // move to the previous week
        navigateToStats(driver, actionProvider)

        // get the date of the first sets
        val date = driver.findElementByCssSelector("div.c-MatchSummaryCard:nth-child(1) > h2:nth-child(1)")
        val dateText = date.text
        println(dateText)

        // open the stats for the game
        openGameStats(driver, actionProvider)

        // get the team name & match score
        val team1Name =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[1]/div/strong")
        val team2Name =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[3]/div/strong")
        val team1Score =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]")
        val team2Score =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[3]")
        val team1NameText = team1Name.text
        val team2NameText = team2Name.text
        val team1ScoreInt = team1Score.text.toInt()
        val team2ScoreInt = team2Score.text.toInt()
        println("$team1ScoreInt $team2ScoreInt")


        val numOfGames = team1ScoreInt + team2ScoreInt
        println(numOfGames)

        val matchGames = arrayListOf<SplGame>()

        for (gameNum in 1..numOfGames) {

            println("#### Game $gameNum Stats ###")

            // Scrape stats for winning team
            val winningTeamStatsHeader =
                driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2")
            val winningTeamText = winningTeamStatsHeader.text.substringBefore(" ")
            val winningTeamBasicStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div"

            val winningTeamBasicStats = scrapeBasicStats(
                driver = driver,
                xpath = winningTeamBasicStatsXPath,
                teamName = winningTeamText
            )
            // get damage stats for winning team
            val winningTeamAdditionalStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
            val winningTeamAdditionalStatsTable = driver.findElementsByXPath(winningTeamAdditionalStatsXPath)

            val winningTeamCompleteStats = scrapeAdditionalStats(
                additionalStatsTable = winningTeamAdditionalStatsTable,
                teamGameStats = winningTeamBasicStats
            )

            // scrape stats for losing team
            val losingTeamBasicStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
            val losingTeam =
                driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2")
            val losingTeamText = losingTeam.text.substringBefore(" ")

            val losingTeamBasicStats = scrapeBasicStats(
                driver = driver,
                xpath = losingTeamBasicStatsXPath,
                teamName = losingTeamText
            )

            val losingTeamAdditionalStatsXPath =
                "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[5]/div/table/tbody/tr"
            val losingTeamAdditionalStatsTable = driver.findElementsByXPath(losingTeamAdditionalStatsXPath)

            val losingTeamCompleteStats = scrapeAdditionalStats(
                additionalStatsTable = losingTeamAdditionalStatsTable,
                teamGameStats = losingTeamBasicStats
            )

            val game = SplGame(team1Stats = winningTeamCompleteStats, team2Stats = losingTeamCompleteStats)
            matchGames.add(game)

            // navigate to next game
            if (gameNum < numOfGames) {
                val nextGameXPath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[1]/div[${gameNum+1}]"
                val nextGameButton = driver.findElementByXPath(nextGameXPath)
                actionProvider.clickAndHold(nextGameButton).build().perform()
                actionProvider.release(nextGameButton).build().perform()
            }

        }

        splMatch = SplMatch(date = dateText,
            team1 = enumValueOf(team1NameText),
            team2 = enumValueOf(team2NameText),
            team1Score = team1ScoreInt,
            team2Score = team2ScoreInt,
            games = matchGames
        )

        println("Spl Match Stats")
        println(splMatch)

    } finally {
        driver.quit()
    }

    // score the match

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
        driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[1]/div[1]/div[10]/a[2]")
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
    val heading = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/h1")
    println(heading.text)
}

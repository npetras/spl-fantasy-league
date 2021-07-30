import data.collection.SplPlayerStats
import org.openqa.selenium.By
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions

fun main() {
    val driver = FirefoxDriver()

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
        println(date.text)
        val jadeDragons =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[1]/div[1]/div[6]/div[1]/p")
        println(jadeDragons.text)

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
        println(team1Name.text)
        println(team2Name.text)
        println(team1Score.text)
        println(team2Score.text)

        // Scrape stats for team 1
        val game1WinningTeam =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2")
        val game1WinningTeamText = game1WinningTeam.text.substringBefore(" ")
        val game1WinningTeamStatsXpath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div"

        val game1WinningTeamStats = scrapeBasicStats(
            driver = driver, xpath = game1WinningTeamStatsXpath, teamName = game1WinningTeamText
        )

        // get damage stats for team 1
        val g1WinTeamAdditionalStatsXpath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
        val g1WinTeamAdditionalStats = driver.findElementsByXPath(g1WinTeamAdditionalStatsXpath)

        for ((i, player) in g1WinTeamAdditionalStats.withIndex()) {
            val name = player.findElement(By.className("name")).text.uppercase()
            val playerDamage = player.findElement(By.className("damage")).text
            val playerDamageInt = playerDamage.replace(",", "").toInt()

            val playerStats = game1WinningTeamStats[i]

            if (playerStats.name == name) {
                playerStats.playerDamage = playerDamageInt
            } else {
                System.err.println("Names do not match")
            }

            println(playerStats)
        }

        println()

        // get stats for team 2
        val game2WinningTeamStatsXpath =
            "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[4]/div/div/div"
        val game2WinningTeam =
            driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[3]/h2")
        val game2WinningTeamText = game2WinningTeam.text.substringBefore(" ")

        val game2WinningTeamStats = scrapeBasicStats(
            driver = driver, xpath = game2WinningTeamStatsXpath,
            teamName = game2WinningTeamText
        )

    } finally {
        driver.quit()
    }
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

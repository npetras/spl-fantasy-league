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
        val jadeDragons = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[3]/div[1]/div/div[1]/div[1]/div[6]/div[1]/p")
        println(jadeDragons.text)

        // open the stats for the game
        openGameStats(driver, actionProvider)

        // get the team name & match score
        val team1Name = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[1]/div/strong")
        val team2Name = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[3]/div/strong")
        val team1Score = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]")
        val team2Score = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[1]/div/div[2]/div[1]/div[3]")
        println(team1Name.text)
        println(team2Name.text)
        println(team1Score.text)
        println(team2Score.text)

        // Algorithm for scraping game stats
        val game1WinningTeam = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[1]/h2")
        val game1WinningTeamText = game1WinningTeam.text.substringBefore(" ")
        println(game1WinningTeamText)

        val game1WinningTeamStatsXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div"
        val game1WinningTeamPlayer1Name = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div[10]")
        val game1WinningTeamPlayer1Role = driver.findElementByXPath("/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[1]/div/div/div[11]")
        println(game1WinningTeamPlayer1Name.text)
        println(game1WinningTeamPlayer1Role.text)

        val game1WinningTeamStats = arrayListOf<SplPlayerStats>()
        var divNum = 10

        for (i in 1..5) {

            val name = driver.findElementByXPath("$game1WinningTeamStatsXpath[$divNum]").text
            divNum += 1
            val role = driver.findElementByXPath("$game1WinningTeamStatsXpath[$divNum]").text
            divNum += 2
            val kills = driver.findElementByXPath("$game1WinningTeamStatsXpath[$divNum]").text
            divNum +=1
            val deaths = driver.findElementByXPath("$game1WinningTeamStatsXpath[$divNum]").text
            divNum +=1
            val assists = driver.findElementByXPath("$game1WinningTeamStatsXpath[$divNum]").text
            divNum += 4

            val playerStats = SplPlayerStats(name = name.uppercase(),
                splTeam = enumValueOf(game1WinningTeamText),
                role = enumValueOf(role),
                kills = kills.toInt(),
                deaths = deaths.toInt(),
                assists = assists.toInt()
            )
            println(playerStats)
            game1WinningTeamStats.add(playerStats)
        }
        println()

        // get damage stats for team 1
        val g1WinTeamAdditionalStatsXpath = "/html/body/div/div/div[1]/div/div[2]/div/div[2]/div/div[3]/div[2]/div[2]/div/table/tbody/tr"
        val g1WinTeamAdditionalStats = driver.findElementsByXPath(g1WinTeamAdditionalStatsXpath)
        // for every tr

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

    } finally {
        driver.quit()
    }
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

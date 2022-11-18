package api

import data.scoring.SplPlayerMatchScore
import data.SmiteRole
import data.SplTeamName

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FantasyScoreApi {
    @GetMapping("/matchScores")
    fun getPlayerScores(): SplPlayerMatchScore {
        return SplPlayerMatchScore(
            name="Jarcorr",
            role=SmiteRole.HUNTER,
            team = SplTeamName.SOLAR,
            gameScores = arrayListOf(30.5, 20.0, 10.0),
            overallMatchScore = 65.5
        )
    }
}
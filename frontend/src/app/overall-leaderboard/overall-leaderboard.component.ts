import {Component, Injectable, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {FantasyTeam} from "../data/FantasyTeam";
import {ScoresService} from "../scores.service";

@Injectable()
@Component({
  selector: 'app-overall-leaderboard',
  templateUrl: './overall-leaderboard.component.html',
  styleUrls: ['./overall-leaderboard.component.css']
})
export class OverallLeaderboardComponent implements OnInit {

  fantasyTeams$: Observable<FantasyTeam[]> | undefined

  constructor(private scoresService: ScoresService) { }

  ngOnInit(): void {
    this.fantasyTeams$ = this.scoresService.getOverallScores()
  }

}

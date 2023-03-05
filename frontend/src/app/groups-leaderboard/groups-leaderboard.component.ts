import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import { FantasyTeamGroup } from '../data/FantasyTeamGroup';
import {ScoresService} from "../scores.service";

@Component({
  selector: 'app-groups-leaderboard',
  templateUrl: './groups-leaderboard.component.html',
  styleUrls: ['./groups-leaderboard.component.css']
})
export class GroupsLeaderboardComponent implements OnInit {

  fantasyGroups$: Observable<FantasyTeamGroup[]> | undefined

  constructor(private scoresService: ScoresService) { }

  ngOnInit(): void {
    this.fantasyGroups$ = this.scoresService.getGroupScores()
  }

}

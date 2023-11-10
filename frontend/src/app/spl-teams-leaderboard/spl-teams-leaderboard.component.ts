import { Component, OnInit } from '@angular/core';
import { SplTeamScores } from '../data/SplTeamScores';

@Component({
  selector: 'app-spl-teams-leaderboard',
  templateUrl: './spl-teams-leaderboard.component.html',
  styleUrls: ['./spl-teams-leaderboard.component.css']
})
export class SplTeamsLeaderboardComponent implements OnInit {
  // playerScores for the Team
  // teamScores: SplTeamScores[];

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {SplMatchScore} from "../model/SplMatchScore";
import {MatchScoresService} from "./match-scores.service";

@Component({
  selector: 'app-match-scores',
  templateUrl: './match-scores.component.html',
  styleUrls: ['./match-scores.component.css']
})
export class MatchScoresComponent implements OnInit {
  error: any
  matchScores: Array<SplMatchScore> | undefined

  constructor(private matchScoresService: MatchScoresService) { }

  ngOnInit(): void {
    this.matchScoresService.getMatchStats()
      .subscribe(
        (data: Array<SplMatchScore>) => {
          this.matchScores = data;
        }
      )
  }

}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SplMatchScore} from "../model/SplMatchScore";

@Injectable({
  providedIn: 'root'
})
export class MatchScoresService {
  matchStatsUrl = "http://localhost:8081/weekMatchScores"

  constructor(private http: HttpClient) { }

  getMatchStats() {
    return this.http.get<Array<SplMatchScore>>(this.matchStatsUrl)
  }
}

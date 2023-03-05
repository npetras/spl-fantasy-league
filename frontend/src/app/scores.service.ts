import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ScoresService {

  url = "http://localhost:8081"

  constructor(private httpClient: HttpClient) { }

  public getOverallScores(): Observable<any> {
    return this.httpClient.get(this.url + "/overallScores")
  }

  public getGroupScores(): Observable<any> {
    return this.httpClient.get(this.url + "/groupScores")
  }
}

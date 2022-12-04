import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FantasyTeam} from "./data/FantasyTeam";

@Injectable({
  providedIn: 'root'
})
export class ScoresService {

  url = "http://localhost:8081/scores"

  constructor(private httpClient: HttpClient) { }

  public get(): Observable<any> {
    return this.httpClient.get(this.url)
  }
}

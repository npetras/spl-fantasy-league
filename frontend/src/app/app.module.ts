import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, Router, RouterModule } from '@angular/router'

import { AppComponent } from './app.component';
import { OverallLeaderboardComponent } from './overall-leaderboard/overall-leaderboard.component';
import {HttpClientModule} from "@angular/common/http";
import { GroupsLeaderboardComponent } from './groups-leaderboard/groups-leaderboard.component';
import { HeaderComponent } from './header/header.component';
// import { SplTeamsLeaderboardComponent } from './app/spl-teams-leaderboard/spl-teams-leaderboard.component';

const appRoutes: Routes = [
  { path: '', component: OverallLeaderboardComponent },
  { path: 'overall', component: OverallLeaderboardComponent },
  { path: 'groups',  component: GroupsLeaderboardComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    OverallLeaderboardComponent,
    GroupsLeaderboardComponent,
    HeaderComponent,
    // SplTeamsLeaderboardComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

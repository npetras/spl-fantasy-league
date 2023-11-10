import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplTeamsLeaderboardComponent } from './spl-teams-leaderboard.component';

describe('SplTeamsLeaderboardComponent', () => {
  let component: SplTeamsLeaderboardComponent;
  let fixture: ComponentFixture<SplTeamsLeaderboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SplTeamsLeaderboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SplTeamsLeaderboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

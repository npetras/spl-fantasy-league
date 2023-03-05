import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsLeaderboardComponent } from './groups-leaderboard.component';

describe('GroupsLeaderboardComponent', () => {
  let component: GroupsLeaderboardComponent;
  let fixture: ComponentFixture<GroupsLeaderboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupsLeaderboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsLeaderboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

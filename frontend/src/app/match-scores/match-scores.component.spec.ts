import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchScoresComponent } from './match-scores.component';

describe('MatchStatsComponent', () => {
  let component: MatchScoresComponent;
  let fixture: ComponentFixture<MatchScoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchScoresComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatchScoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

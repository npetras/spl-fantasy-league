import { TestBed } from '@angular/core/testing';

import { MatchScoresService } from './match-scores.service';

describe('MatchStatsService', () => {
  let service: MatchScoresService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatchScoresService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

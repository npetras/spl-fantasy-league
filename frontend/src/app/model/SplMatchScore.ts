export class SplMatchScore{
  homeTeamName: string;
  awayTeamName: string;
  homeTeamScore: number;
  awayTeamScore: number;
  homeTeamScores: Array<SplMatchScore>;
  awayTeamScores: Array<SplMatchScore>


  constructor(homeTeamName: string = "None", awayTeamName: string = "None", homeTeamScore: number = 0,
              awayTeamScore: number = 0, homeTeamScores: Array<SplMatchScore> = [],
              awayTeamScores: Array<SplMatchScore> = []) {
    this.homeTeamName = homeTeamName;
    this.awayTeamName = awayTeamName;
    this.homeTeamScore = homeTeamScore;
    this.awayTeamScore = awayTeamScore;
    this.homeTeamScores = homeTeamScores;
    this.awayTeamScores = awayTeamScores;
  }
}

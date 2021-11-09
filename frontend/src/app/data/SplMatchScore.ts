export class SplMatchScore{
  homeTeamName: string;
  awayTeamName: string;
  homeTeamScore: Array<SplMatchScore>;
  awayTeamScore: Array<SplMatchScore>


  constructor(homeTeamName: string = "None", awayTeamName: string = "None", homeTeamScore: Array<SplMatchScore> = [],
              awayTeamScore: Array<SplMatchScore> = []) {
    this.homeTeamName = homeTeamName;
    this.awayTeamName = awayTeamName;
    this.homeTeamScore = homeTeamScore;
    this.awayTeamScore = awayTeamScore;
  }
}

export class SplPlayerMatchScore{
  name: string;
  role: string;
  team: string;
  gameScores: number[];
  overallMatchScore: number

  constructor(name: string = "", role: string= "", team: string = "None", gameScores: number[] = [],
              overallMatchScore: number = 0.0) {
    this.name = name
    this.role = role
    this.team = team
    this.gameScores = gameScores
    this.overallMatchScore = overallMatchScore
  }
}


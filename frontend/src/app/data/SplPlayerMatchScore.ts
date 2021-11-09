export class SplPlayerMatchScore{
  name: string;
  role: string;
  team: string;
  teamScores: number[];
  overallMatchScore: number

  constructor(name: string = "", role: string= "", team: string = "None", teamScores: number[] = [],
              overallMatchScore: number = 0.0) {
    this.name = name
    this.role = role
    this.team = team
    this.teamScores = teamScores
    this.overallMatchScore = overallMatchScore
  }
}


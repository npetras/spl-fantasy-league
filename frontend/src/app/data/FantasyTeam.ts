export class FantasyTeam{
  fantasyPlayerName: string;
  fantasyTeamGroup: string;
  solo: string;
  soloScore: number;
  jungle: string;
  jungleScore: number;
  mid: string;
  midScore: number;
  support: string;
  supportScore: number;
  hunter: string;
  hunterScore: number;
  totalTeamScore: number;


  constructor(fantasyPlayerName: string = "", fantasyTeamGroup: string = "", solo: string = "", soloScore: number = 0.0,
              jungle: string = "", jungleScore: number = 0.0, mid: string = "", midScore: number = 0.0,
              support: string = "", supportScore: number = 0.0, hunter: string = "", hunterScore: number = 0.0,
              totalTeamScore: number = 0.0) {
    this.fantasyPlayerName = fantasyPlayerName
    this.fantasyTeamGroup = fantasyTeamGroup
    this.solo = solo
    this.soloScore = soloScore
    this.jungle = jungle
    this.jungleScore = jungleScore
    this.mid = mid
    this.midScore = midScore
    this.support = support
    this.supportScore = supportScore
    this.hunter = hunter
    this.hunterScore = hunterScore
    this.totalTeamScore = totalTeamScore
  }
}


import { FantasyTeam } from "./FantasyTeam";

export class FantasyTeamGroup {
  groupName: string;
  fantasyTeams: FantasyTeam[];

  constructor(groupName: string, fantasyTeams: FantasyTeam[]) {
    this.groupName = groupName
    this.fantasyTeams = fantasyTeams
  }
}



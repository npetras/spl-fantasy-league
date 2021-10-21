CREATE TYPE smiteRole AS ENUM ('Solo', 'Jungle', 'Mid', 'Support', 'Carry');

CREATE TABLE splPlayers (
    ign VARCHAR(25) PRIMARY KEY,
    thisSmiteRole smiteRole,
);

CREATE TABLE splTeams (
    teamName VARCHAR(30),
    solo VARCHAR(25) REFERENCES splPlayers(ign),
    jungle VARCHAR(25) REFERENCES splPlayers(ign),
    mid VARCHAR(25) REFERENCES splPlayers(ign),
    support VARCHAR(25) REFERENCES splPlayers(ign),
    carry VARCHAR(25) REFERENCES splPlayers(ign)
);

CREATE TABLE leagueTeams (
    teamName VARCHAR(30),
    playerName VARCHAR(25),
    solo VARCHAR(25),
    jungle VARCHAR(25),
    mid VARCHAR(25),
    support VARCHAR(25),
    carry VARCHAR(25),
    week1Pts NUMERIC,
    week2Pts NUMERIC,
    week3Pts NUMERIC,
    week4Pts NUMERIC,
    week5Pts NUMERIC,
    week6Pts NUMERIC,
    week7Pts NUMERIC,
    week8Pts NUMERIC,
    PRIMARY KEY(teamName, playerName)

    CONSTRAINT fkSolo
        FOREIGN KEY (solo)
        REFERENCES splPlayers(ign)
    CONSTRAINT fkJungle
        FOREIGN KEY (jungle)
        REFERENCES splPlayers(ign)
    CONSTRAINT fkMid
        FOREIGN KEY (mid)
        REFERENCES splPlayers(ign)
    CONSTRAINT fkSupport
        FOREIGN KEY (support)
        REFERENCES splPlayers(ign)
    CONSTRAINT fkCarry
        FOREIGN KEY (carry)
        REFERENCES splPlayers(ign)
);

CREATE TABLE leagueGroups (
    team1Name VARCHAR(30),
    team1PlayerName VARCHAR(25),
    team2Name VARCHAR(30),
    team2PlayerName VARCHAR(25),
    team3Name VARCHAR(30),
    team3PlayerName VARCHAR(25),
    team4Name VARCHAR(30),
    team4PlayerName VARCHAR(25),
    team5Name VARCHAR(30),
    team5PlayerName VARCHAR(25),
    
    CONSTRAINT fkTeam1
        FOREIGN KEY (team1Name, team1PlayerName)
        REFERENCES leagueTeams(teamName, playerName)
    CONSTRAINT fkTeam2
        FOREIGN KEY (team2Name, team2PlayerName)
        REFERENCES leagueTeams(teamName, playerName)
    CONSTRAINT fkTeam3
        FOREIGN KEY (team3Name, team3PlayerName)
        REFERENCES leagueTeams(teamName, playerName)
    CONSTRAINT fkTeam4
        FOREIGN KEY (team4Name, team4PlayerName)
        REFERENCES leagueTeams(teamName, playerName)
    CONSTRAINT fkTeam5
        FOREIGN KEY (team5Name, team5PlayerName)
        REFERENCES leagueTeams(teamName, playerName)
);

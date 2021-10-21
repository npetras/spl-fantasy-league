CREATE TYPE smiteRole AS ENUM ('Solo', 'Jungle', 'Mid', 'Support', 'Carry');

CREATE TABLE splPlayers (
    ign VARCHAR(25),
    thisSmiteRole smiteRole,
    PRIMARY KEY(ign)
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
    solo VARCHAR(25) REFERENCES splPlayers(ign),
    jungle VARCHAR(25) REFERENCES splPlayers(ign),
    mid VARCHAR(25) REFERENCES splPlayers(ign),
    support VARCHAR(25) REFERENCES splPlayers(ign),
    carry VARCHAR(25) REFERENCES splPlayers(ign),
    week1Pts NUMERIC,
    week2Pts NUMERIC,
    week3Pts NUMERIC,
    week4Pts NUMERIC,
    week5Pts NUMERIC,
    week6Pts NUMERIC,
    week7Pts NUMERIC,
    week8Pts NUMERIC,
    PRIMARY KEY(teamName, playerName)
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
    
    FOREIGN KEY (team1Name, team1PlayerName) REFERENCES leagueTeams(teamName, playerName),
    FOREIGN KEY (team2Name, team2PlayerName) REFERENCES leagueTeams(teamName, playerName),
    FOREIGN KEY (team3Name, team3PlayerName) REFERENCES leagueTeams(teamName, playerName),
    FOREIGN KEY (team4Name, team4PlayerName) REFERENCES leagueTeams(teamName, playerName),
    FOREIGN KEY (team5Name, team5PlayerName) REFERENCES leagueTeams(teamName, playerName)
);

INSERT INTO splPlayers 
    (ign, thisSmiteRole)
VALUES
    ('fineokay', 'Solo'),
    ('Sam4soccer2', 'Jungle');

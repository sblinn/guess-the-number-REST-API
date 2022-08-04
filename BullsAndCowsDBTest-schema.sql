DROP DATABASE IF EXISTS BullsAndCowsDBTest;

CREATE DATABASE BullsAndCowsDBTest;

USE BullsAndCowsDBTest; 

CREATE TABLE Game (
	gameID INT PRIMARY KEY AUTO_INCREMENT,
    answer CHAR(4) NOT NULL,
    isComplete BOOLEAN NOT NULL DEFAULT 0 -- 0 being game is incomplete
);

CREATE TABLE Round (
	roundID INT PRIMARY KEY AUTO_INCREMENT, 
    gameID INT NOT NULL,
    guess CHAR(4) NOT NULL, 
    `time` DATETIME NOT NULL,
    result CHAR(7) NOT NULL
);

ALTER TABLE Round 
	ADD CONSTRAINT fk_Round_Game
		FOREIGN KEY (gameID)
        REFERENCES Game(gameID);
        
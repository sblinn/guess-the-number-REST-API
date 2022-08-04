
package com.sblinn.bullsandcows.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sara Blinn
 */
public class Game {

    private int gameID; 
    private String answer;
    private boolean isComplete; // default false/0 being game is incomplete
    private List<Round> gameRounds = new ArrayList<>();

    public Game() {
        this.isComplete = false;
    }
    
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public List<Round> getGameRounds() {
        return gameRounds;
    }

    public void setGameRounds(List<Round> gameRounds) {
        this.gameRounds = gameRounds;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.gameID;
        hash = 89 * hash + Objects.hashCode(this.answer);
        hash = 89 * hash + (this.isComplete ? 1 : 0);
        hash = 89 * hash + Objects.hashCode(this.gameRounds);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Game other = (Game) obj;
        if (this.gameID != other.gameID) {
            return false;
        }
        if (this.isComplete != other.isComplete) {
            return false;
        }
        if (!Objects.equals(this.answer, other.answer)) {
            return false;
        }
        return Objects.equals(this.gameRounds, other.gameRounds);
    }
    
}

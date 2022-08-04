
package com.sblinn.bullsandcows.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author Sara Blinn
 */
public class Round {

    private int roundID;
    private int gameID; 
    private String guess;
    private LocalDateTime time;
    private String result;

    
    public int getRoundID() {
        return roundID;
    }

    public void setRoundID(int roundID) {
        this.roundID = roundID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm:ss");
        String timeAsText = time.format(formatter);
        LocalDateTime formattedTime
                = LocalDateTime.parse(timeAsText, formatter);
        this.time = formattedTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + this.roundID;
        hash = 13 * hash + this.gameID;
        hash = 13 * hash + Objects.hashCode(this.guess);
        hash = 13 * hash + Objects.hashCode(this.time);
        hash = 13 * hash + Objects.hashCode(this.result);
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
        final Round other = (Round) obj;
        if (this.roundID != other.roundID) {
            return false;
        }
        if (this.gameID != other.gameID) {
            return false;
        }
        if (!Objects.equals(this.guess, other.guess)) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        return Objects.equals(this.time, other.time);
    }
    
}

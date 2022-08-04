package com.sblinn.bullsandcows.service;

import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import java.util.List;

/**
 *
 * @author Sara Blinn
 */
public interface BullsAndCowsService {
    
    Game createGame(Game game) throws
            DuplicatePrimaryKeyException,
            InvalidDataException,
            DataNotFoundException;
    
    Game getGameByID(int gameID) throws
            DataNotFoundException;
    
    List<Game> getAllGames() throws 
            DataNotFoundException;
    
    boolean updateGame(Game updatedGame) throws
            DataNotFoundException,
            InvalidDataException;
    
    
    Round createRound(Round round) throws
            DuplicatePrimaryKeyException,
            DataNotFoundException,
            InvalidDataException;
    
    Round getRoundByID(int roundID) throws
            DataNotFoundException;
    
    List<Round> getAllRoundsForGame(int gameID) throws
            DataNotFoundException;
    
}

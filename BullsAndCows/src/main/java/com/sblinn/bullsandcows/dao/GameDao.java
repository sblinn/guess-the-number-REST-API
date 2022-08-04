package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dto.Game;
import java.util.List;

/**
 *
 * @author Sara Blinn
 */
public interface GameDao {
        
    List<Game> getAllGames();
    
    Game createGame(Game game);
    
    Game getGameByID(int gameID);
    
    boolean updateGame(Game updatedGame);
    
    boolean deleteGameByID(int gameID);
    
}

package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dto.Round;
import java.util.List;

/**
 *
 * @author Sara Blinn
 */
public interface RoundDao {
    
    Round createRound(Round round);
    
    Round getRoundByID(int roundID);
    
    List<Round> getAllRounds();
    
    List<Round> getAllRoundsByGameID(int gameID);
    
    boolean updateRound(Round updatedRound);
    
    boolean deleteRoundByID(int roundID);
    
}

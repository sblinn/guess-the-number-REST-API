
package com.sblinn.bullsandcows.controller;

import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import com.sblinn.bullsandcows.service.BullsAndCowsService;
import com.sblinn.bullsandcows.service.DataNotFoundException;
import com.sblinn.bullsandcows.service.DuplicatePrimaryKeyException;
import com.sblinn.bullsandcows.service.InvalidDataException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sara Blinn
 */
@RestController
@RequestMapping("/api/bullsandcows")
public class BullsAndCowsController {

    @Autowired
    private BullsAndCowsService service;
    
    
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game) throws 
            DuplicatePrimaryKeyException, 
            InvalidDataException, 
            DataNotFoundException {
        
        return service.createGame(game);        
    }
    
    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round createRoundGuess(@RequestBody Round round) throws 
            DuplicatePrimaryKeyException, 
            DataNotFoundException, 
            InvalidDataException {
        
        return service.createRound(round);
    }
    
    @GetMapping("/games")
    public List<Game> getAllGames() throws DataNotFoundException {
        
        List<Game> games = service.getAllGames();
        for (Game game : games) {
            if (game.getIsComplete() == false) {
                game.setAnswer("****");
            }
        }
        return games;
    }
    
    @GetMapping("/game/{gameID}")
    public Game getGameByGameID(@PathVariable int gameID) 
            throws DataNotFoundException {
        
        Game game = service.getGameByID(gameID);
        if (game.getIsComplete() == false) {
            game.setAnswer("****");
        }
        return game;
    }
    
    @GetMapping("/rounds/{gameID}")
    public List<Round> getGameRoundsByGameID(@PathVariable int gameID) {
        List<Round> rounds;
        try {
            rounds = service.getAllRoundsForGame(gameID);
        } catch (DataNotFoundException e) {
            return null;
        }
        
        return rounds;
    }
    
}

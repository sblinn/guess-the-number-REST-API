
package com.sblinn.bullsandcows.service;

import com.sblinn.bullsandcows.dao.GameDao;
import com.sblinn.bullsandcows.dao.RoundDao;
import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


/**
 *
 * @author Sara Blinn
 */
@Service
@Profile("database")
public class BullsAndCowsServiceImpl implements BullsAndCowsService {

    @Autowired
    GameDao gameDao;
    @Autowired
    private RoundDao roundDao;
    
    
    @Override
    public Game createGame(Game game) throws 
            DuplicatePrimaryKeyException,
            InvalidDataException,
            DataNotFoundException {
        
        // check if gameID already exists -- no duplicate PKs allowed
        if (game.getGameID() != 0) {
            try {
                final String DUPLICATE_PK_EXCEPTION_MSG
                        = "Error: Game by that ID already exists.";
                if (gameDao.getGameByID(game.getGameID()) != null) {
                    throw new DuplicatePrimaryKeyException(
                            DUPLICATE_PK_EXCEPTION_MSG);
                }
            } catch (NullPointerException e) {
                // ignore -- indicates that the gameID is unique
            }
        }      
        
        Game createdGame = new Game();
        createdGame.setGameID(game.getGameID());
        createdGame.setAnswer(game.getAnswer());
        createdGame.setIsComplete(game.getIsComplete());
        createdGame.setGameRounds(game.getGameRounds());
        // check for missing required fields and invalid data
        validateGameData(createdGame);  
        // returns game with ID
        createdGame = gameDao.createGame(game);
        
        return createdGame;
    }

    @Override
    public Game getGameByID(int gameID) throws 
            DataNotFoundException {
        
        Game game; 

        final String DATA_NOT_FOUND_MSG
                = "Error: No game exists by game ID " + gameID;
        try {
            game = gameDao.getGameByID(gameID);         
            if (game == null) {
                throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
            }
        } catch (NullPointerException e) {
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        return game;
    }

    @Override
    public List<Game> getAllGames() throws 
            DataNotFoundException {
        
        List<Game> games;
        
        try {
            games = gameDao.getAllGames();
        } catch (NullPointerException e) {
            final String DATA_NOT_FOUND_MSG
                    = "Error: No existing games.";
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        return games;
    }

    @Override
    public boolean updateGame(Game updatedGame) throws 
            DataNotFoundException,
            InvalidDataException {
        
        int gameID = updatedGame.getGameID();
        
        if (gameID == 0) {
            final String MISSING_DATA_MSG
                    = "Error: Game missing required fields: gameID.";
            throw new DataNotFoundException(MISSING_DATA_MSG);
        }
        
        final String DATA_NOT_FOUND_MSG
                = "Error: No game by game ID "
                + gameID + " exists to update.";
        try {
            Game game = gameDao.getGameByID(gameID);
            if (game == null) {
                throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
            }
        } catch (NullPointerException e) {
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        validateGameData(updatedGame);
        return gameDao.updateGame(updatedGame);
    }

    
    
    @Override
    public Round createRound(Round round) throws 
            DuplicatePrimaryKeyException,
            DataNotFoundException,
            InvalidDataException {
        
        if (round.getRoundID() != 0) {
            try {
                final String DUPLICATE_PK_EXCEPTION_MSG
                        = "Error: Round by that ID already exists.";
                if (roundDao.getRoundByID(round.getRoundID()) != null) {
                    throw new DuplicatePrimaryKeyException(
                            DUPLICATE_PK_EXCEPTION_MSG);
                }
            } catch (NullPointerException e) {
                // ignore -- indicates that the roundID is unique
            }
        }
        
        Round createdRound = new Round();
        createdRound.setRoundID(round.getRoundID());
        createdRound.setGameID(round.getGameID());
        createdRound.setGuess(round.getGuess());
        
        if (round.getTime() != null) {
            createdRound.setTime(round.getTime());
        }
        if (round.getResult() != null) {
            createdRound.setResult(round.getResult());        
        }
        
        // sets the time to now if none set already
        validateRoundData(createdRound);
        calculateRoundResult(createdRound);
        
        return roundDao.createRound(createdRound);
    }

    @Override
    public Round getRoundByID(int roundID) throws 
            DataNotFoundException {
        
        Round round;

        final String DATA_NOT_FOUND_MSG
                = "Error: No round exists by round ID "
                + roundID + ".";
        try {
            round = roundDao.getRoundByID(roundID);         
            if (round == null) {
                throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
            }
        } catch (NullPointerException e) {
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        return round;
    }

    @Override
    public List<Round> getAllRoundsForGame(int gameID) throws 
            DataNotFoundException {
        
        List<Round> rounds; 
        try {
            rounds = roundDao.getAllRoundsByGameID(gameID);
        } catch (NullPointerException e) {
            final String DATA_NOT_FOUND_MSG
                    = "Error: No rounds exists for Game " 
                    + gameID + ".";
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        return rounds;
    }

    
    
    private void validateGameData(Game game) throws
            DataNotFoundException,
            InvalidDataException {

        String answer = game.getAnswer();
        final String DATA_NOT_FOUND_MSG 
                = "Error: Game missing required field for answer.";
        if (answer == null) {
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }

        final String INVALID_DATA_LENGTH_MSG 
                = "Error: Game answer must contain only 4 numbers.";
        if (answer.length() != 4) {
            throw new InvalidDataException(INVALID_DATA_LENGTH_MSG);
        }
                
        try {
            int answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            final String INVALID_DATA_MSG 
                = "Error: Game answer must contain all "
                + "unique characters.";
            throw new InvalidDataException(INVALID_DATA_MSG);
        }
                
        String[] answerArr = answer.split("");
        final String INVALID_DUPLICATE_CHAR_MSG 
                = "Error: Game answer must contain all "
                + "unique characters.";
        for (int i = 0; i < answer.length() -1; i++) {
            for (int j = i + 1; j < answer.length(); j++) {
                if (answerArr[i].equals(answerArr[j])) {
                    throw new InvalidDataException(
                            INVALID_DUPLICATE_CHAR_MSG);
                }
            }
        }
        
        // check if input isComplete value is incompatible 
        if (game.getIsComplete() == true 
                && game.getGameRounds().isEmpty()) {
            game.setIsComplete(false);
        } else {
            // checks if the game has list of Rounds, then 
            // updates isComplete 
            try {
                List<Round> gameRounds = game.getGameRounds();
                for (Round round : gameRounds) {
                    if (round.getGuess().equals(answer)) {
                        game.setIsComplete(true);
                        return;
                    }
                }
            } catch (NullPointerException e) {
                game.setIsComplete(false);
            }
        }
    }
    
    private void validateRoundData(Round round) throws 
            DataNotFoundException, 
            InvalidDataException {
        
        final String DATA_NOT_FOUND_MSG
                = "Error: Round missing required fields.";
        if (round.getGameID() == 0 || round.getGuess() == null ) {
            throw new DataNotFoundException(DATA_NOT_FOUND_MSG);
        }
        
        final String INVALID_DATA_MSG 
                = "Error: Guess must contain only 4 numbers.";
        if (round.getGuess().length() != 4) {
            throw new InvalidDataException(INVALID_DATA_MSG);
        }
        
        if (round.getTime() == null) {
            round.setTime(LocalDateTime.now());
        }
    }
    
    private void calculateRoundResult(Round round) {
        Game game = gameDao.getGameByID(round.getGameID());
        String answerStr = game.getAnswer();
        String[] answer = answerStr.split("");
        String[] guess = round.getGuess().split("");
        int e = 0;
        int p = 0;
        
        // if the round matches the answer, 
        // update Game to isComplete = true
        if (answerStr.equals(round.getGuess())) {
            round.setResult("e:4:p:0");
            game.setIsComplete(true);
            gameDao.updateGame(game);
        } else {
            for (int index = 0; index < 4; index++) {
                if (answer[index].equals(guess[index])) {
                    e++;
                } else if (answerStr.contains(guess[index])) {
                    p++;
                }
            }
            round.setResult("e:" + e + ":p:" + p);
        }
    }
    
}

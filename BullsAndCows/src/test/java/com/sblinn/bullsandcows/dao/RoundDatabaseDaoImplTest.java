package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sara Blinn
 */
@SpringBootTest
@Transactional
public class RoundDatabaseDaoImplTest {

    @Autowired
    private GameDao testGameDao;
    
    @Autowired
    private RoundDao testRoundDao;
    
    public RoundDatabaseDaoImplTest() {
    }
    

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        // delete all data from the test database
        List<Round> rounds = testRoundDao.getAllRounds();
        for (Round round : rounds) {
            testRoundDao.deleteRoundByID(round.getRoundID());
        }
        
        List<Game> games = testGameDao.getAllGames();
        for (Game game : games) {
            testGameDao.deleteGameByID(game.getGameID());
        }
        
        // add two games to the test database.
        // gameID is a foreign key in Round table.
        Game game1 = new Game(); // gameID = 1;
        game1.setGameID(1);
        game1.setAnswer("1230");
        game1.setIsComplete(false);
        
        Game game2 = new Game(); // gameID = 2;
        game2.setGameID(2);
        game2.setAnswer("0321");
        game2.setIsComplete(false);
        
        testGameDao.createGame(game1);
        testGameDao.createGame(game2);
    }

    @AfterEach
    public void tearDown() {
        // delete all data from the test database
        List<Round> rounds = testRoundDao.getAllRounds();
        for (Round round : rounds) {
            testRoundDao.deleteRoundByID(round.getRoundID());
        }

        List<Game> games = testGameDao.getAllGames();
        for (Game game : games) {
            testGameDao.deleteGameByID(game.getGameID());
        }
    }

    @Test
    public void testCreateAndGetRoundByID() {
        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("1234");
        testRound.setTime(LocalDateTime.now());
        testRound.setResult("e:3:p:0");
        
        Round createdRound = testRoundDao.createRound(testRound);      
        Round retrievedRound = testRoundDao.getRoundByID(1);
        
        assertEquals(testRound, createdRound, 
                "Round returned upon creation should equal the "
                        + "test Round.");
        assertEquals(retrievedRound, createdRound, 
                "Round returned upon creation should equal the "
                        + "retrieved Round.");
        assertEquals(retrievedRound.getTime(), createdRound.getTime(),
                "Time is causing issues.");
        assertEquals(testRound, retrievedRound,
                "Retrieved round should equal the test Round.");
    }

    @Test
    public void testGetAllRounds() {
        Round testRound1 = new Round();
        testRound1.setRoundID(1);
        testRound1.setGameID(1);
        testRound1.setGuess("1234");
        testRound1.setTime(LocalDateTime.now());
        testRound1.setResult("e:3:p:0");
        
        Round testRound2 = new Round();
        testRound2.setRoundID(2);
        testRound2.setGameID(2);
        testRound2.setGuess("1235");
        testRound2.setTime(LocalDateTime.now());
        testRound2.setResult("e:0:p:3");
        
        Round createdRound1 = testRoundDao.createRound(testRound1);
        Round createdRound2 = testRoundDao.createRound(testRound2);
        
        List<Round> testRounds = testRoundDao.getAllRounds();
        
        assertEquals(2, testRounds.size(), 
                "List of Rounds should contain only 2 Rounds.");
        assertTrue(testRounds.contains(createdRound1),
                "List of Rounds should contain testRound1.");
        assertTrue(testRounds.contains(createdRound2), 
                "List of Rounds should contain testRound2.");  
    }

    @Test
    public void testGetAllRoundsByGameID() {
        Round testRound1 = new Round();
        testRound1.setRoundID(1);
        testRound1.setGameID(1);
        testRound1.setGuess("1234");
        testRound1.setTime(LocalDateTime.now());
        testRound1.setResult("e:3:p:0");
        
        Round testRound2 = new Round();
        testRound2.setRoundID(2);
        testRound2.setGameID(2);
        testRound2.setGuess("1235");
        testRound2.setTime(LocalDateTime.now());
        testRound2.setResult("e:3:p:0");
        
        Round createdRound1 = testRoundDao.createRound(testRound1);
        Round createdRound2 = testRoundDao.createRound(testRound2);
        
        List<Round> testRoundsGame1 = 
                testRoundDao.getAllRoundsByGameID(1); 
        List<Round> testRoundsGame2 = 
                testRoundDao.getAllRoundsByGameID(2); 
        
        assertEquals(1, testRoundsGame1.size(), 
                "List of Rounds should contain only 1 Round.");
        assertEquals(1, testRoundsGame2.size(),
                "List of Rounds should contain only 1 Round.");
        assertTrue(testRoundsGame1.contains(createdRound1),
                "List of Rounds should contain testRound1.");
        assertTrue(testRoundsGame2.contains(createdRound2), 
                "List of Rounds should contain testRound2."); 
    }

    @Test
    public void testUpdateRound() {
        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("1234");
        testRound.setTime(LocalDateTime.now());
        testRound.setResult("e:3:p:0");
        
        // make new Round with different values to update testRound.
        // roundID must be the same in order to update the correct Round.
        // updates: gameID, guess, time
        Round testRoundUpdate = new Round();
        testRoundUpdate.setRoundID(1); // should be same as testRound
        testRoundUpdate.setGameID(2);
        testRoundUpdate.setGuess("1235");
        testRoundUpdate.setTime(LocalDateTime.now());
        testRoundUpdate.setResult("e:3:p:0");
        
        Round createdRound = testRoundDao.createRound(testRound);      
        boolean isUpdated = testRoundDao.updateRound(testRoundUpdate);
        Round updatedTestRound = testRoundDao.getRoundByID(1);
        List<Round> rounds = testRoundDao.getAllRounds();
        
        assertEquals(true, isUpdated, 
                "Method updateRound should return true.");
        assertEquals(updatedTestRound, testRoundUpdate,
                "Retrieved testRound should be the same"
                        + " as the updatedRound.");  
        assertEquals(1, rounds.size(), 
                "There should only be 1 Round in the database.");
    }

    @Test
    public void testDeleteRoundByID() {
        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("1234");
        testRound.setTime(LocalDateTime.now());
        testRound.setResult("e:3:p:0");
        
        Round createdRound = testRoundDao.createRound(testRound);
        boolean isDeleted = testRoundDao.deleteRoundByID(1);
        List<Round> testRounds = testRoundDao.getAllRounds();
        
        assertNotNull(createdRound,
                "Created test Round should not return null if "
                        + "created successfully.");
        assertEquals(true, isDeleted, 
                "Method deleteRoundByID should return true.");
        assertTrue(testRounds.isEmpty(), 
                "List of Rounds should be empty.");      
    }

}

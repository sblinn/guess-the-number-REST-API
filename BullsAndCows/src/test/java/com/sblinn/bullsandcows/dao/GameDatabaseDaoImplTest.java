package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
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
public class GameDatabaseDaoImplTest {
    
    @Autowired
    private GameDao testGameDao;
    
    @Autowired
    private RoundDao testRoundDao;
    
    public GameDatabaseDaoImplTest() {
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
    public void testCreateAndGetGameByID() {
        Game testGame = new Game(); 
        testGame.setGameID(1);
        testGame.setAnswer("1230");
        testGame.setIsComplete(false);

        Game createdGame = testGameDao.createGame(testGame);
        Game retrievedGame = testGameDao.getGameByID(1);
        
        assertEquals(testGame, createdGame, 
                "Game returned upon creation should equal test Game.");
        assertEquals(testGame, retrievedGame, 
                "Retrieved game should equal test Game.");
    }

    @Test
    public void testGetAllGames() {
        Game testGame1 = new Game(); 
        testGame1.setGameID(1);
        testGame1.setAnswer("1230");
        testGame1.setIsComplete(false);

        Game testGame2 = new Game(); 
        testGame2.setGameID(2);
        testGame2.setAnswer("0321");
        testGame2.setIsComplete(false);

        testGameDao.createGame(testGame1);
        testGameDao.createGame(testGame2);
        List<Game> games = testGameDao.getAllGames();
        
        assertTrue(games.size() == 2, 
                "Database should contain 2 games.");
        assertTrue(games.contains(testGame1),
                "Database should contain testGame1.");
        assertTrue(games.contains(testGame2),
                "Database should contain testGame2.");
    }

    @Test
    public void testUpdateGame() {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1230");
        testGame.setIsComplete(false);

        Game testGameUpdates = new Game();
        testGameUpdates.setGameID(testGame.getGameID());
        testGameUpdates.setAnswer("0321");
        testGameUpdates.setIsComplete(true);
        
        testGameDao.createGame(testGame);
        boolean isUpdated = testGameDao.updateGame(testGameUpdates);
        Game updatedGame = testGameDao.getGameByID(1);
        
        assertTrue(isUpdated, "GameDao method updateGame "
                + "should return true.");
        assertEquals(testGameUpdates, updatedGame,
                "Retrieved Game should equal the updated test Game.");
    }

    @Test
    public void testDeleteGameByID() {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1230");
        testGame.setIsComplete(false);

        Game createdGame = testGameDao.createGame(testGame);
        boolean isDeleted = testGameDao.deleteGameByID(1);
        List<Game> games = testGameDao.getAllGames();

        assertNotNull(createdGame, "Created test Game should not "
                + "return null if created successfully.");
        assertTrue(isDeleted, "GameDao method deleteGameByID "
                + "should return true.");
        assertEquals(0, games.size(), "Database should not contain "
                + "any remaining Games.");
    }
    
}

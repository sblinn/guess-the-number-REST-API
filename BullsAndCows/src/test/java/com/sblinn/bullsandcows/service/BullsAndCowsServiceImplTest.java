package com.sblinn.bullsandcows.service;

import com.sblinn.bullsandcows.dao.GameDao;
import com.sblinn.bullsandcows.dao.GameDatabaseDaoImpl;
import com.sblinn.bullsandcows.dao.RoundDao;
import com.sblinn.bullsandcows.dao.RoundDatabaseDaoImpl;
import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
public class BullsAndCowsServiceImplTest {

    @Autowired
    BullsAndCowsService testService;

    public BullsAndCowsServiceImplTest() {
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
        GameDao testGameDao = new GameDatabaseDaoImpl();
        RoundDao testRoundDao = new RoundDatabaseDaoImpl();

        try {
            List<Round> rounds = testRoundDao.getAllRounds();
            for (Round round : rounds) {
                testRoundDao.deleteRoundByID(round.getRoundID());
            }
        } catch (NullPointerException e) {
        }

        try {
            List<Game> games = testGameDao.getAllGames();
            for (Game game : games) {
                testGameDao.deleteGameByID(game.getGameID());
            }
        } catch (NullPointerException e) {
        }
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testCreateAndGetGameByID() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        testGame.setIsComplete(false);

        // create Game and retrieve it 
        Game createdGame = testService.createGame(testGame);
        Game retrievedGame = testService.getGameByID(1);

        assertEquals(testGame, createdGame,
                "Game returned upon creation should equal "
                + "the test Game.");
        assertEquals(createdGame, retrievedGame,
                "Game returned upon creation should equal "
                + "the retrieved Game.");
        assertEquals(testGame, retrievedGame,
                "Retrieved Game should equal test Game.");
    }

    @Test
    public void testCreateDuplicateGame() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false when value not supplied
        
        Game duplicateTestGame = new Game();
        duplicateTestGame.setGameID(1);
        duplicateTestGame.setAnswer("1234");

        testService.createGame(testGame);

        try {
            testService.createGame(duplicateTestGame);
            fail("Creating Game with existing gameID should throw "
                    + "DuplicatePrimaryKeyException.");
        } catch (DuplicatePrimaryKeyException e) {
            // test passed if exception thrown
        }
    }

    @Test
    public void testBlankAnswerCreateGame() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("");
        testGame.setIsComplete(false);

        try {
            testService.createGame(testGame);
            fail("Creating Game with empty answer should throw "
                    + "InvalidDataExcepiton.");
        } catch (InvalidDataException e) {
            // pass if exception thrown
        }
    }

    @Test
    public void testInvalidAnswerCreateGame() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("123"); // answer should be 4 char.
        testGame.setIsComplete(false);

        try {
            testService.createGame(testGame);
            fail("Creating Game with invalid answer should throw "
                    + "InvalidDataException.");
        } catch (InvalidDataException e) {
            // pass if exception thrown
        }
    }
    
        @Test
    public void testDuplicateCharAnswerCreateGame() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("4444");

        try {
            testService.createGame(testGame);
            fail("Creating Game with answer containing duplicate "
                    + "characters should throw "
                    + "InvalidDataExcepiton.");
        } catch (InvalidDataException e) {
            // pass if exception thrown
        }
    }
    
    @Test
    public void testGetGameNotExists() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");

        testService.createGame(testGame);
        Game retrievedGame = testService.getGameByID(1);

        assertNotNull(retrievedGame, "Game should have been created.");
        
        try {
            testService.getGameByID(5);
            fail("Attempting to retrieve Game that does not exist "
                    + "should throw Exception.");
        } catch (DataNotFoundException e) {
            // passed
        }
    }

    @Test
    public void testGetAllGames() throws Exception {
        Game testGame1 = new Game();
        testGame1.setGameID(1);
        testGame1.setAnswer("1234");
        testGame1.setIsComplete(false);

        Game testGame2 = new Game();
        testGame2.setGameID(2);
        testGame2.setAnswer("4321");
        testGame2.setIsComplete(false);

        testService.createGame(testGame1);
        testService.createGame(testGame2);

        List<Game> games = testService.getAllGames();

        assertEquals(2, games.size(),
                "Database should contain 2 Games.");
        assertTrue(games.contains(testGame1));
        assertTrue(games.contains(testGame2));
    }

    @Test
    public void testUpdateGame() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        testGame.setIsComplete(false);

        Game testGameUpdates = new Game();
        testGameUpdates.setGameID(1);
        testGameUpdates.setAnswer("4321");
        testGameUpdates.setIsComplete(true);

        Game createdGame = testService.createGame(testGame);
        boolean isUpdated = testService.updateGame(testGameUpdates);
        Game updatedGame = testService.getGameByID(1);

        assertNotNull(createdGame, "Game should have been created.");
        assertTrue(isUpdated, "Method updateGame should return true.");
        assertEquals(testGameUpdates, updatedGame,
                "Retrieved Game should equal the updated Game.");
    }
    
    @Test
    public void testUpdateGameNotExists() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");

        // attempt to update a game that doesn't exist
        try {
            boolean isUpdated = testService.updateGame(testGame);
            fail("Attempt to update Game that doesn't exist should "
                    + "throw Exception.");
        } catch (DataNotFoundException e) {
            // passed
        }
    }
    
    @Test
    public void testUpdateGameNoID() throws Exception {
        Game testGameNoID = new Game(); // default int = 0 (gameID)
        testGameNoID.setAnswer("1234");
        
        // attempt to update a game that doesn't exist
        try {
            assertEquals(0, testGameNoID.getGameID(), 
                    "Default ID integer should be 0");
            boolean isUpdated = testService.updateGame(testGameNoID);
            fail("Attempt to update Game without gameID should "
                    + "throw Exception.");
        } catch (DataNotFoundException e) {
            // passed
        }
    }

    /**
     * Test createRound and getRoundByID methods using a Round with
     * the correct guess, allowing service methods to calculate the
     * result. Test also checks that Game data is updated when a
     * correct guess is given in a Round.
     *
     * @throws Exception
     */
    @Test
    public void testCreateAndGetRoundByID() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        testGame.setIsComplete(false);

        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("1234");
        testRound.setTime(LocalDateTime.now());
        // service methods will calculate the Round's result 
        // expected result --> e:4:p:0

        Game createdGame = testService.createGame(testGame);
        Round createdRound = testService.createRound(testRound);
        Round retrievedRound = testService.getRoundByID(1);
        Game completedGame = testService.getGameByID(1);
        // set testRound's expected result value, to compare to the 
        // calculated result
        testRound.setResult("e:4:p:0");

        assertEquals(testRound.getRoundID(), createdRound.getRoundID(), "RoundID");
        assertEquals(testRound.getGameID(), createdRound.getGameID(), "GameID");
        assertEquals(testRound.getGuess(), createdRound.getGuess(), "Guess");
        assertEquals(testRound.getResult(), createdRound.getResult(), "Result");
        assertEquals(testRound.getTime(), createdRound.getTime(), "Time");

        assertEquals(testRound, createdRound,
                "Created Round should equal the test Round.");
        assertEquals(testRound, retrievedRound,
                "Retrieved Round should equal the test Round.");
        assertEquals(createdRound, retrievedRound,
                "CreatedRound should equal the retrieved Round.");
        assertEquals("e:4:p:0", retrievedRound.getResult(),
                "Result should be e:4:p:0.");
        assertFalse(createdGame.getIsComplete(),
                "isComplete value for Game before correct guess "
                + "should equal false.");
        assertTrue(completedGame.getIsComplete(),
                "Game for Round should be updated so that "
                + "isComplete = true.");
    }

    /**
     * Tests that result is calculated properly when an incorrect
     * guess is supplied.
     *
     * @throws Exception
     */
    @Test
    public void testCreateRoundIncorrectGuess() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
//        testGame.setIsComplete(false);
        // default is set to false

        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("4231");
        testRound.setTime(LocalDateTime.now());
        // service methods will calculate the Round's result 
        // expected result --> e:2:p:2

        Game createdGame = testService.createGame(testGame);
        Round createdRound = testService.createRound(testRound);

        Round retrievedRound = testService.getRoundByID(1);
        Game completedGame = testService.getGameByID(1);
        // set testRound's expected result value, to compare to the 
        // calculated result
        testRound.setResult("e:2:p:2");

        assertEquals(testRound, createdRound,
                "Created Round should equal the test Round.");
        assertEquals(testRound, retrievedRound,
                "Retrieved Round should equal the test Round.");
        assertEquals(createdRound, retrievedRound,
                "CreatedRound should equal the retrieved Round.");
        assertEquals("e:2:p:2", retrievedRound.getResult(),
                "Result should be e:2:p:2.");
        assertFalse(createdGame.getIsComplete(),
                "isComplete value for Game before Round "
                + "should equal false.");
        assertFalse(completedGame.getIsComplete(),
                "isComplete value for Game after Round "
                + "should remain false.");
    }

    @Test
    public void testCreateRoundDuplicateID() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false

        Round testRound = new Round();
        testRound.setRoundID(1);
        testRound.setGameID(1);
        testRound.setGuess("4231");
        // time and result are set by service layer when not supplied
        
        Round duplicateTestRound = new Round();
        duplicateTestRound.setRoundID(1);
        duplicateTestRound.setGameID(1);
        duplicateTestRound.setGuess("4231");

        Game createdGame = testService.createGame(testGame);
        Round createdRound = testService.createRound(testRound);
        Round retrievedRound = testService.getRoundByID(1);
        
        // assert that round created and unsupplied fields filled
        assertNotNull(createdRound, "Round should not be null.");
        assertNotNull(createdRound.getTime(), 
                "Time should not be null.");
        assertNotNull(createdRound.getResult(), 
                "Result should not be null.");
        
        // try to add a Round with the same ID (duplicate)
        try {
            testService.createRound(duplicateTestRound);
            fail("Creating Round with duplicate roundID should "
                    + "throw Exception.");
        } catch (DuplicatePrimaryKeyException e) {
            // passed
        }
    }


    @Test
    public void testCreateRoundNoID() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false
        
        Round testRoundNoID = new Round();
        testRoundNoID.setGameID(1);
        testRoundNoID.setGuess("4231");
        // time and result are set by service layer when not supplied

        Game createdGame = testService.createGame(testGame);
        
        // create testRoundNoID
        Round createdRound = testService.createRound(testRoundNoID);
        Round retrievedRound = 
                testService.getRoundByID(createdRound.getRoundID());
        
        // assert that round created and roundID created
        assertNotNull(createdRound, "Round should not be null.");
        assertNotNull(retrievedRound.getRoundID(), 
                "ID should not be null, ID assigned upon creation.");
    }
    
    @Test
    public void testCreateRoundNoGameID() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false
        
        Round testRoundNoGameID = new Round();
        testRoundNoGameID.setRoundID(1);
        testRoundNoGameID.setGuess("4231");

        Game createdGame = testService.createGame(testGame);
        
        // create testRoundNoGameID
        try {
            testService.createRound(testRoundNoGameID);
            fail("Attempt to create Round without gameID should"
                    + " throw Exception.");
        } catch (DataNotFoundException e) {
            // passed
        }
    }
    
    @Test
    public void testCreateRoundNoGuess() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false
        
        Round testRoundNoGuess = new Round();  
        testRoundNoGuess.setRoundID(1);
        testRoundNoGuess.setGameID(1);

        Game createdGame = testService.createGame(testGame);
        
        // create testRoundNoGuess
        try {
            Round createdRound 
                    = testService.createRound(testRoundNoGuess);
            fail("Attempt to create Round with no guess value "
                    + "should throw Exception.");
        } catch (DataNotFoundException e) {
            // passed
        }
    }

    @Test
    public void testCreateRoundInvalidGuess() throws Exception {
        Game testGame = new Game();
        testGame.setGameID(1);
        testGame.setAnswer("1234");
        // default isComplete = false
        
        Round testRoundInvalidGuess = new Round();
        testRoundInvalidGuess.setRoundID(1);
        testRoundInvalidGuess.setGameID(1);
        testRoundInvalidGuess.setGuess("12"); // valid = 4 char

        Game createdGame = testService.createGame(testGame);

        // create testRoundInvalidGuess
        try {
            Round createdRound = 
                testService.createRound(testRoundInvalidGuess);
            fail("Attempt to create Round with invalid guess value "
                    + "should throw Exception.");
        } catch (InvalidDataException e) {
            // passed
        }
    }

    @Test
    public void testGetAllRoundsForGame() throws Exception {
        Game testGame1 = new Game();
        testGame1.setGameID(1);
        testGame1.setAnswer("1234");
        
        Game testGame2 = new Game();
        testGame2.setGameID(2);
        testGame2.setAnswer("1235");
        
        Round testRound1 = new Round();
        testRound1.setRoundID(1);
        testRound1.setGameID(1);
        testRound1.setGuess("1200");
        testRound1.setTime(LocalDateTime.now());
        testRound1.setResult("e:2:p:0");
        
        Round testRound2 = new Round();
        testRound2.setRoundID(2);
        testRound2.setGameID(1);
        testRound2.setGuess("1230");
        testRound2.setTime(LocalDateTime.now());
        testRound2.setResult("e:3:p:0");
        
        Round testRound3 = new Round();
        testRound3.setRoundID(3);
        testRound3.setGameID(2);
        testRound3.setGuess("1230");
        testRound3.setTime(LocalDateTime.now());
        testRound3.setResult("e:3:p:0");
        
        Game createdGame1 = testService.createGame(testGame1);
        Game createdGame2 = testService.createGame(testGame2);
        
        Round createdRound1 = testService.createRound(testRound1);
        Round createdRound2 = testService.createRound(testRound2);
        Round createdRound3 = testService.createRound(testRound3);
        
        List<Round> game1rounds = testService.getAllRoundsForGame(1);
        List<Round> game2rounds = testService.getAllRoundsForGame(2);
        
        assertEquals(2, game1rounds.size(), 
                "Game 1 should have 2 Rounds.");
        assertEquals(1, game2rounds.size(),
                "Game 2 should have 1 Round.");
        assertTrue(game1rounds.contains(createdRound1),
                "Game 1 should have roundID 1.");
        assertTrue(game1rounds.contains(createdRound2),
                "Game 1 should have roundID 2.");
        assertTrue(game2rounds.contains(createdRound3), 
                "Game 2 should have roundID 3.");
    }

}

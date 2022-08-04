
package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dao.RoundDatabaseDaoImpl.RoundMapper;
import com.sblinn.bullsandcows.dto.Game;
import com.sblinn.bullsandcows.dto.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Sara Blinn
 */
@Repository
@Profile("database")
public class GameDatabaseDaoImpl implements GameDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    

    /**
     * Adds Game to Database, returns Game with ID if ID not 
     * previously supplied.
     * @param game
     * @return Game 
     */
    @Override
    public Game createGame(Game game) {
        
        if (game.getGameID() != 0) {
            final String INSERT_GAME
                = "INSERT INTO Game(gameID, answer, isComplete) "
                + "VALUES(?,?,?);";
            
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_GAME, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, game.getGameID());
                statement.setString(2, game.getAnswer());
                statement.setBoolean(3, game.getIsComplete());
                return statement;
            });
        } else if (game.getGameID() == 0) {
            final String INSERT_GAME_NO_ID
                = "INSERT INTO Game(answer, isComplete) "
                + "VALUES(?,?);";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_GAME_NO_ID, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, game.getAnswer());
                statement.setBoolean(2, game.getIsComplete());
                return statement;
            }, keyHolder);
            
            game.setGameID(keyHolder.getKey().intValue());
        } else {
            final String INSERT_GAME_DEFAULT
                    = "INSERT INTO Game(answer) "
                    + "VALUES(?);";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_GAME_DEFAULT, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, game.getAnswer());
                return statement;
            }, keyHolder);

            game.setGameID(keyHolder.getKey().intValue());
        }     

        return game;
    }

    @Override
    @Transactional
    public Game getGameByID(int gameID) {
        try {
            final String GET_GAME
                    = "SELECT * FROM Game "
                    + "WHERE gameID = ?;";
            Game retrievedGame = jdbcTemplate.queryForObject(
                    GET_GAME, new GameMapper(), gameID);
            retrievedGame.setGameRounds(getRoundsForGame(gameID));
            return retrievedGame;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Game> getAllGames() {
        final String SELECT_ALL
                = "SELECT * FROM Game "
                + "ORDER BY gameID;";
        List<Game> games = jdbcTemplate.query(
                SELECT_ALL, new GameMapper());
        addRoundsToGames(games);
        return games;
    }
    
    /**
     * Updates Game and returns true if an update occurred.
     * 
     * @param updatedGame
     * @return boolean 
     */
    @Override
    public boolean updateGame(Game updatedGame) {
        final String UPDATE_GAME 
                = "UPDATE Game "
                + "SET gameID = ?, answer = ?, isComplete = ? "
                + "WHERE gameID = ?;";
        int numRowsUpdated = jdbcTemplate.update(
                UPDATE_GAME, 
                updatedGame.getGameID(),
                updatedGame.getAnswer(),
                (updatedGame.getIsComplete() ? 1:0),
                updatedGame.getGameID());
        return numRowsUpdated == 1;
    }

    /**
     * Deletes Game by gameID if it exists, returns true if a
     * deletion occurred. 
     * 
     * @param gameID
     * @return boolean 
     */
    @Override
    @Transactional
    public boolean deleteGameByID(int gameID) {
        // delete all rounds associated with the game from Round table 
        final String DELETE_GAME_ROUNDS 
                = "DELETE FROM Round "
                + "WHERE gameID = ?;";
        jdbcTemplate.update(DELETE_GAME_ROUNDS, gameID);
        
        // delete the game from the Game table
        final String DELETE_GAME
                = "DELETE FROM Game "
                + "WHERE gameID = ?;";
        int numRowsDeleted = jdbcTemplate.update(DELETE_GAME, gameID);
        return numRowsDeleted == 1;
    }

    private List<Round> getRoundsForGame(int gameID) {
        try {
            final String GET_GAME_ROUNDS
                    = "SELECT * FROM Round "
                    + "WHERE gameID = ?;";
            List<Round> gameRounds = jdbcTemplate.query(GET_GAME_ROUNDS,
                    new RoundMapper(), gameID);
            return gameRounds;
        } catch (NullPointerException e) {
            System.out.println("No rounds for " + gameID);
            return new ArrayList<>();
        } 
    }
    
    private void addRoundsToGames(List<Game> games) {
       for (Game game : games) {
           game.setGameRounds(getRoundsForGame(game.getGameID()));
       }
    }
    
    
    /**
     * Creates a Game object from a row in the Game table. 
     */
    public static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int rowNum) 
                throws SQLException {
            
            Game game = new Game();
            game.setGameID(rs.getInt("gameID"));
            game.setAnswer(rs.getString("answer"));
            game.setIsComplete(rs.getBoolean("isComplete"));
            
            return game;
        }
        
    }
}

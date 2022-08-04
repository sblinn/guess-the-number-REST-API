
package com.sblinn.bullsandcows.dao;

import com.sblinn.bullsandcows.dto.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
public class RoundDatabaseDaoImpl implements RoundDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    

    /**
     * Creates and adds a given game Round to the database and returns
     * the input Round with the new roundID.
     * 
     * @param round
     * @return Round with new roundID
     */
    @Override
    @Transactional
    public Round createRound(Round round) {
        
        if (round.getRoundID() != 0) {
            final String INSERT_ROUND
                    = "INSERT INTO Round (roundID, gameID, guess, "
                    + "time, result) "
                    + "VALUES (?, ?, ?, ?, ?);";

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_ROUND, 
                        Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, round.getRoundID());
                statement.setInt(2, round.getGameID());
                statement.setString(3, round.getGuess());
                statement.setTimestamp(4, 
                        Timestamp.valueOf(round.getTime()));
                statement.setString(5, round.getResult());
                return statement;
            });
        } else {
            final String INSERT_ROUND_NO_ID
                    = "INSERT INTO Round (gameID, guess, "
                    + "time, result) "
                    + "VALUES (?, ?, ?, ?);";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        INSERT_ROUND_NO_ID, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, round.getGameID());
                statement.setString(2, round.getGuess());
                statement.setTimestamp(3, Timestamp.valueOf(round.getTime()));
                statement.setString(4, round.getResult());
                return statement;
            }, keyHolder);

            round.setRoundID(keyHolder.getKey().intValue());
        }

        return round;
    }

    /**
     * Gets a game Round from the database by its roundID, or returns
     * null if no Round found. 
     * 
     * @param roundID
     * @return Round or null
     */
    @Override
    public Round getRoundByID(int roundID) {
        // queryForObject throws exception if no object is retrieved.
        try {
            final String GET_ROUND
                    = "SELECT * FROM Round "
                    + "WHERE roundID = ?;";
            Round retrievedRound = jdbcTemplate.queryForObject(
                    GET_ROUND, new RoundMapper(), roundID);
            return retrievedRound;
        } catch (DataAccessException e) {
            return null;
        } 
    }

    @Override
    public List<Round> getAllRounds() {
        final String SELECT_ALL
                = "SELECT * FROM Round;";
        List<Round> rounds = jdbcTemplate.query(
                SELECT_ALL, new RoundMapper());
        return rounds;
    }
    
    @Override
    public List<Round> getAllRoundsByGameID(int gameID) {
        final String GET_GAME_ROUNDS
                = "SELECT * FROM Round "
                + "WHERE gameID = ? "
                + "ORDER BY `time`;";
        List<Round> gameRounds = jdbcTemplate.query(
                GET_GAME_ROUNDS, new RoundMapper(), gameID);
        return gameRounds;
    }

    @Override
    public boolean updateRound(Round updatedRound) {
        final String UPDATE_ROUND
                = "UPDATE Round "
                + "SET roundID = ?, gameID = ?, guess = ?, "
                + "time = ?, result = ? "
                + "WHERE roundID = ?;";
        
        int numRowsUpdated = jdbcTemplate.update(UPDATE_ROUND,
                updatedRound.getRoundID(),
                updatedRound.getGameID(),
                updatedRound.getGuess(),
                Timestamp.valueOf(updatedRound.getTime()),
                updatedRound.getResult(),
                updatedRound.getRoundID());
        return numRowsUpdated == 1;
    }

    @Override
    public boolean deleteRoundByID(int roundID) {
        final String DELETE_ROUND
                = "DELETE FROM Round "
                + "WHERE roundID = ?;";
        int numRowsDeleted
                = jdbcTemplate.update(DELETE_ROUND, roundID);
        return numRowsDeleted == 1;
    }

    
    /**
     * Creates a game Round object from a row in the Round table. 
     */
    public static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int rowNum) 
                throws SQLException {
            
            Round round = new Round(); 
            round.setRoundID(rs.getInt("roundID"));
            round.setGameID(rs.getInt("gameID"));
            round.setGuess(rs.getString("guess"));
            round.setTime(rs.getTimestamp("time").toLocalDateTime());
            round.setResult(rs.getString("result"));
            
            return round;
        }
        
    } 
    
}

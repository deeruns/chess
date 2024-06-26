package dataAccess;

import DataAccess.DataAccessException;
import Models.GameData;
import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{
    //add database and tables if they don't exist
    public SqlGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameData = new ArrayList<>();
        try( var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM games";
            try(var prepStatement = conn.prepareStatement(statement)){
                ResultSet rs = prepStatement.executeQuery();
                while(rs.next()){
                    ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    gameData.add(new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame));
                    //return gameData;
                }
            }

        }
        catch (SQLException exception){
            throw new DataAccessException("Error: bad request");
        }
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM games WHERE gameID=?";
            try( var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setInt(1, gameID);
                try(var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(Integer.parseInt(rs.getString("gameID")), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                    }
                    else{
                        throw new DataAccessException("Error: bad request");
                    }
                }
            }
        }
        catch(SQLException exception){
            //game doesn't exist
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public GameData getGameName(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameName=?";
            try (var prepStatement = conn.prepareStatement(statement)) {
                prepStatement.setString(1, gameName);
                try (var rs = prepStatement.executeQuery()) {
                    if (rs.next()) {
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                    } else {
                        // Game with the specified gameName doesn't exist
                        throw new DataAccessException("Error: Game with name '" + gameName + "' does not exist");
                    }
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error: Database error occurred: " + exception.getMessage());
        }
    }



    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            // Check if the gameName already exists
            String checkStatement = "SELECT * FROM games WHERE gameName = ?";
            try (var checkPrepStatement = conn.prepareStatement(checkStatement)) {
                checkPrepStatement.setString(1, gameName);
                try (var rs = checkPrepStatement.executeQuery()) {
                    if (rs.next()) {
                        // Game with the same name already exists, throw an exception
                        throw new DataAccessException("Error: already exists");
                    }
                }
            }

            // Insert the new game
            String insertStatement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try (var prepStatement = conn.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS)) {
                ChessGame game = new ChessGame();
                String jsonGame = new Gson().toJson(game);
                prepStatement.setString(1, null);
                prepStatement.setString(2, null);
                prepStatement.setString(3, gameName);
                prepStatement.setString(4, jsonGame);
                prepStatement.executeUpdate();
                var rs = prepStatement.getGeneratedKeys();
                if (rs.next()) {
                    GameData gameData = new GameData(rs.getInt(1), null, null, gameName, game);
                    return gameData;
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error: bad request");
        }
        return null;
    }

    @Override
    public void addUser(int gameID, String username, String userColor) throws DataAccessException {
        getGame(gameID);
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games Where gameID=?";
            try (var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setInt(1, gameID);

                try(var rs = prepStatement.executeQuery()) {
                    if (rs.next()) {
                        if ("WHITE".equals(userColor)) {
                            String whiteUsername = rs.getString("whiteUsername");
                            if (whiteUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            } else {
                                var whiteStatement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
                                try (var setWhiteUser = conn.prepareStatement(whiteStatement)) {
                                    setWhiteUser.setString(1, username);
                                    setWhiteUser.setInt(2, gameID);

                                    setWhiteUser.executeUpdate();
                                }
                            }
                        } else if ("BLACK".equals(userColor)) {
                            String blackUsername = rs.getString("blackUsername");
                            if (blackUsername != null) {
                                throw new DataAccessException("Error: already taken");
                            } else {
                                var blackStatement = "UPDATE games SET blackUsername=? WHERE gameID=?";
                                try (var setBlackUser = conn.prepareStatement(blackStatement)) {
                                    setBlackUser.setString(1, username);
                                    setBlackUser.setInt(2, gameID);

                                    setBlackUser.executeUpdate();
                                }
                            }
                        }
                    }
                }
                }
            }
        catch(SQLException exception){
            throw new DataAccessException("Error: already taken");
        }
    }


    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE games";
        DatabaseManager.executeUpdate(statement);
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
            `gameID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `whiteUsername` VARCHAR(255),
            `blackUsername` VARCHAR(255),
            `gameName` VARCHAR(255) NOT NULL,
            `game` BLOB
            )
            """
    };
}

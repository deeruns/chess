package dataAccess;

import Models.GameData;
import Models.UserData;
import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SqlGameDAO implements GameDAO{
    //add database and tables if they don't exist
    public SqlGameDAO() throws DataAccessException{
        configureDatabase();
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
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM games WHERE gameName=?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, String.valueOf(gameName));
                try(var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(Integer.parseInt(rs.getString("gameID")), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
                    }
                }
            }
        }
        catch(SQLException exception){
            //game doesn't exist
            throw new DataAccessException("Error: bad request");
        }
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            //no gameID because it is auto generated
            var statement =  "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try(var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                ChessGame game = new ChessGame();
                String jsongame = new Gson().toJson(game);
                prepStatement.setString(1, null);
                prepStatement.setString(2, null);
                prepStatement.setString(3, gameName);
                prepStatement.setString(4, jsongame);
                prepStatement.executeUpdate();
                var rs = prepStatement.getGeneratedKeys();
                if (rs.next()) {
                    GameData gameData = new GameData(rs.getInt(1), null, null, gameName, game);
                    return gameData;
                }
            }

        }
        catch(SQLException exception){
            throw new DataAccessException("Error: bad request");
        }
//        try{
//           var statement =  "INSERT INTO game (gameID,whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
//           DatabaseManager.executeUpdate(statement, gameName);
//        }
//        catch(DataAccessException exception){
//            throw new DataAccessException("Error: bad request");
//        }
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

    public void addWhiteUser(int gameID, String username) throws DataAccessException {
        var statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }
    public void addBlackUser(int gameID, String username) throws DataAccessException {
        var statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
        DatabaseManager.executeUpdate(statement, username, gameID);
    }


    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        var statement = "DELETE FROM games WHERE gameID=?";
        DatabaseManager.executeUpdate(statement, gameID);
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
    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement: createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }

        }
        catch (SQLException exception){
            throw new DataAccessException(String.format("Unable to configure database: %s", exception.getMessage()));
        }
    }
}

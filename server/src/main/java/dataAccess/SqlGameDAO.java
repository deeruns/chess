package dataAccess;

import Models.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{
    //add database and tables if they don't exist
    public SqlGameDAO() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGameName(String gameName) {
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(int gameID, String username, String userColor) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
            'gameID' INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
            'whiteUsername' VARCHAR(255),
            'blackUsername' VARCHAR(255),
            'gameName VARCHAR(255) NOT NULL,
            'game' BLOB,
            FOREIGN KEY (whiteUsername) REFERENCES users(username),
            FOREIGN KEY (blackUsername) REFERENCES users(username)
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

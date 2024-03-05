package dataAccess;

import Models.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO{
    //add database and tables if they don't exist
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

    }
}

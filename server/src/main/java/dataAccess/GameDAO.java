package dataAccess;

import DataAccess.DataAccessException;
import Models.GameData;

import java.util.Collection;

public interface GameDAO {
    public Collection<GameData> listGames() throws DataAccessException, DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public GameData getGameName(String gameName) throws DataAccessException;
    public GameData createGame(String username) throws DataAccessException;
    public void addUser(int gameID, String username, String userColor) throws DataAccessException;
    void clear() throws DataAccessException;
    void addBlackUser(int gameID, String username) throws DataAccessException;
    void addWhiteUser(int gameID, String username) throws DataAccessException;
}

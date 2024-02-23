package dataAccess;

import Models.GameData;
import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public Collection<GameData> listGames();
    public GameData getGame(int gameID) throws DataAccessException;
    public GameData getGameName(String gameName);
    public GameData createGame(GameData game) throws DataAccessException;
    public void addUser(int gameID, String username, ChessGame.TeamColor userColor) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}

package dataAccess;

import Models.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.List;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int countID = 1;
    private static HashMap<Integer, GameData> gameDataHash = new HashMap<>();

    public Collection<GameData> listGames(){
        // return new ArrayList<>(gameDataHash.values());
        return gameDataHash.values();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("Game does not exist");
        }
        else{
            return gameDataHash.get(gameID);
        }
    }

    public GameData getGameName(String gameName) {
        // find game with a name
        for (GameData game : gameDataHash.values()) {
            if (gameName.equals(game.gameName())) {
                return game;
            }
        }
        return null;
    }
    public GameData createGame(GameData game) throws DataAccessException{
        if (getGameName(game.gameName()) != null){
            throw new DataAccessException("Game name taken");
        }
        else {
            game = new GameData(countID++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
            gameDataHash.put(game.gameID(), game);
            //do i need to return game?
            return game;
        }

    }

    public void addUser(int gameID, String username, ChessGame.TeamColor userColor) throws DataAccessException{
        GameData game = getGame(gameID);
        if (game == null){
            throw new DataAccessException("Game doesn't exist");
        }
        if (userColor == ChessGame.TeamColor.WHITE && game.whiteUsername() != null ||
                userColor == ChessGame.TeamColor.BLACK && game.blackUsername() != null){
            throw new DataAccessException("player color already taken");
        }

        GameData newGame = null;
        if (userColor == ChessGame.TeamColor.WHITE){
            newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        }
        else if (userColor == ChessGame.TeamColor.BLACK){
            newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        gameDataHash.replace(gameID, newGame);
    }
    public void deleteGame(int gameID) throws DataAccessException{
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("game doesn't exist");
        }
        gameDataHash.remove(gameID);
    }
    public void clear() throws DataAccessException{
        gameDataHash.clear();
    }
}

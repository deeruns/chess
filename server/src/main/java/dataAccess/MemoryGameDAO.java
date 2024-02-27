package dataAccess;

import Models.GameData;
import chess.ChessGame;

import java.util.*;
import java.sql.Array;

public class MemoryGameDAO implements GameDAO{
//    private int countID = 1;
    private static HashMap<Integer, GameData> gameDataHash = new HashMap<>();

    public Collection<GameData> listGames(){
        // return new ArrayList<>(gameDataHash.values());
        return gameDataHash.values();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
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
    public GameData createGame(String gameName) throws DataAccessException{
        if (getGameName(gameName) != null){
            throw new DataAccessException("Error: bad request");
        }
        else {
            int gameID = gameIDGenerator();
            if (gameDataHash.containsKey(gameID)){
                throw new DataAccessException("Error: bad request");
            }
            GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
            gameDataHash.put(game.gameID(), game);
            //do i need to return game?
            return game;
        }

    }

    public void addUser(int gameID, String username, String userColor) throws DataAccessException{
        GameData game = getGame(gameID);
        if (game == null){
            throw new DataAccessException("Error: Data Access Exception");
        }
        if (userColor == "WHITE" && game.whiteUsername() != null ||
                userColor == ChessGame.TeamColor.BLACK && game.blackUsername() != null){
            throw new DataAccessException("Error: bad request");
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
            throw new DataAccessException("Error: bad request");
        }
        gameDataHash.remove(gameID);
    }
    public void clear() throws DataAccessException{
        gameDataHash.clear();
    }
    public static int gameIDGenerator(){
        Random random = new Random();
        int gameID = random.nextInt(9000)+1000;
        return gameID;
    }
}

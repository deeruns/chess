package dataAccess;

import Models.GameData;
import chess.ChessGame;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
//    private int countID = 1;
    private static HashMap<Integer, GameData> gameDataHash = new HashMap<>();
    @Override
    public Collection<GameData> listGames(){
        // return new ArrayList<>(gameDataHash.values());
        return gameDataHash.values();
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        //if game doesn't exist
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
        }
        else{
            return gameDataHash.get(gameID);
        }
    }
    @Override
    public GameData getGameName(String gameName) {
        // find game with a name
        for (GameData game : gameDataHash.values()) {
            if (gameName.equals(game.gameName())) {
                return game;
            }
        }
        return null;
    }
    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        //game already exists
        if (getGameName(gameName) != null){
            throw new DataAccessException("Error: bad request");
        }
        else {
            int gameID = gameIDGenerator();
            //if gameID already exists
            if (gameDataHash.containsKey(gameID)){
                throw new DataAccessException("Error: bad request");
            }
            GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
            gameDataHash.put(game.gameID(), game);
            return game;
        }

    }
    @Override
    public void addUser(int gameID, String username, String teamColor) throws DataAccessException {
        GameData game = getGame(gameID);
        //if trying to join a game that doesn't exist
        if (game == null){
            throw new DataAccessException("Error: bad request");
        }
        //if color requested is already taken
        if (Objects.equals(teamColor, "WHITE") && game.whiteUsername() != null ||
                Objects.equals(teamColor, "BLACK") && game.blackUsername() != null){
            throw new DataAccessException("Error: already taken");
        }

        if (Objects.equals(teamColor, "WHITE")){
            GameData newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            gameDataHash.replace(gameID, newGame);
        }
        else if (Objects.equals(teamColor, "BLACK")){
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            gameDataHash.replace(gameID, newGame);
        }
    }
//    @Override
//    public void deleteGame(int gameID) throws DataAccess.DataAccessException{
//        if (!gameDataHash.containsKey(gameID)){
//            throw new DataAccess.DataAccessException("Error: bad request");
//        }
//        gameDataHash.remove(gameID);
//    }
    @Override
    public void clear() throws DataAccessException {
        gameDataHash.clear();
    }

    @Override
    public void addBlackUser(int gameID, String username) throws DataAccessException {
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
        }
        gameDataHash.remove(gameID);
    }

    @Override
    public void addWhiteUser(int gameID, String username) throws DataAccessException {
        if (!gameDataHash.containsKey(gameID)){
            throw new DataAccessException("Error: bad request");
        }
        gameDataHash.remove(gameID);
    }

    public static int gameIDGenerator(){
        Random random = new Random();
        int gameID = random.nextInt(9000)+1000;
        return gameID;
    }
}

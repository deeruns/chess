package service;

import Models.GameData;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class CreateGameService extends ParentService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public GameData createGame(String authToken, String gameName)throws DataAccessException{
        if (gameName == null){
            throw new DataAccessException("Error: bad request");
        }
        authorizeUser(authDAO, authToken);
        GameData newGame = gameDAO.createGame(gameName);
        return newGame;
    }

}

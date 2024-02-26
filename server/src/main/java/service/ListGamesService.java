package service;

import Models.GameData;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ListGamesService extends ParentService{
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void listGames(String authToken) throws DataAccessException{
        authorizeUser(authDAO, authToken);
        gameDAO.listGames();
    }
}

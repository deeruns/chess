package service;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.GameData;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class JoinGameService extends ParentService{
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public void joinGame(String teamColor, int gameID, String authToken) throws DataAccessException {
        Integer newGameID = gameID;
        if(newGameID == null){
            throw new DataAccessException("Error: bad request");
        }
        GameData gameData = gameDAO.getGame(gameID);
        authorizeUser(authDAO, authToken);
        AuthTokenData authData = authDAO.getUser(authToken);
        String username = authData.username();

        gameDAO.addUser(gameID, username, teamColor);
    }
    public void spectatorJoin(int gameID, String authToken) throws DataAccessException {
        authorizeUser(authDAO, authToken);
        gameDAO.getGame(gameID);
    }
}

package service;

import Models.AuthTokenData;
import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.JoinGameRequest;

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
        authorizeUser(authDAO, authToken);
        AuthTokenData authData = authDAO.getUser(authToken);
        String username = authData.username();
        gameDAO.addUser(gameID, username, teamColor);
        //getuser(username)
        //adduser

    }
    public void spectatorJoin(int gameID, String authToken) throws DataAccessException{
        authorizeUser(authDAO, authToken);
        gameDAO.getGame(gameID);
    }
}

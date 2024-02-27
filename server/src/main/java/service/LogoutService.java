package service;

import Models.AuthTokenData;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

public class LogoutService extends ParentService{
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }
    public void logoutUser(String authToken) throws DataAccessException {
        authorizeUser(authDAO, authToken);
        authDAO.deleteAuth(authToken);
    }
}

package service;

import DataAccess.DataAccessException;
import dataAccess.AuthDAO;

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

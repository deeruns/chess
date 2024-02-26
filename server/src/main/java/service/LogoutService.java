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
//    public AuthTokenData authorizeUser(String authToken) throws DataAccessException{
//        AuthTokenData authData = authDAO.getUser(authToken);
//        if (authData == null){
//            throw new DataAccessException("Error: Unauthorized");
//        }
//        else {
//            return authData;
//        }
//    }
}

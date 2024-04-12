package service;

import dataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.UserData;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;

import java.util.Objects;
import java.util.UUID;

public class LoginService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LoginService(AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthTokenData loginUser(String username, String password) throws DataAccessException {
        if (username == null || password == null){
            throw new DataAccessException("Error: bad request");
        }
        UserData userData = userDAO.getUser(username);
        if (userData == null){
            throw new DataAccessException("Error: Unauthorized");
        }
        if (!Objects.equals(userData.password(), password)){
            //wrong password
            throw new DataAccessException("Error: Unauthorized");
        }
        return createAuthToken(username);
    }
    public AuthTokenData createAuthToken(String username) throws DataAccessException{
        UUID auth = UUID.randomUUID();
        AuthTokenData authData =  new AuthTokenData(auth.toString(), username);
        //authDAO.oneAuthTokenPerPlayer(authData);
        authDAO.createMemory(authData);
        return authData;
    }
}

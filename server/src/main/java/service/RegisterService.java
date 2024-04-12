package service;

import dataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.UserData;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;

import java.util.UUID;

public class RegisterService {
    private final AuthDAO authDAO;

    private final UserDAO userDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthTokenData registerUser(String username, String password, String email) throws DataAccessException {
        if (username == null || password == null || email == null){
            throw new DataAccessException("Error: bad request");
        }
        createUser(username, password, email);
        return createAuthToken(username);
    }
    public AuthTokenData createAuthToken(String username) throws DataAccessException{
        UUID auth = UUID.randomUUID();
        AuthTokenData authData =  new AuthTokenData(auth.toString(), username);
        authDAO.createMemory(authData);
        return authData;
    }

    public void createUser(String username, String password, String email) throws DataAccessException{
        UserData registerUser = new UserData(username, password, email);
        userDAO.createUser(registerUser);
    }
}

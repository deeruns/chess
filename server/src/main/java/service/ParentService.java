package service;

import Models.AuthTokenData;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;

public class ParentService {
    public AuthTokenData authorizeUser(AuthDAO authDAO, String authToken) throws DataAccessException {
        AuthTokenData authData = authDAO.getUser(authToken);
        if (authData == null){
            throw new DataAccessException("Error: Unauthorized");
        }
        else {
            return authData;
        }
    }
}

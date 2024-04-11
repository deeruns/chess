package service;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import dataAccess.AuthDAO;

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

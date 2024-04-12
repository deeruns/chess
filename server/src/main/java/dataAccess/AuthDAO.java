package dataAccess;

import DataAccess.DataAccessException;
import Models.AuthTokenData;

public interface AuthDAO {
    public AuthTokenData getUser(String authToken) throws DataAccessException, DataAccessException;
    public void createMemory(AuthTokenData authData) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;

}

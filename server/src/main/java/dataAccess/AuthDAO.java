package dataAccess;

import Models.AuthTokenData;
import Models.UserData;

public interface AuthDAO {
    public AuthTokenData getUser(String authToken) throws DataAccessException;
    public void oneAuthTokenPerPlayer(AuthTokenData authData);
    public void createMemory(AuthTokenData authData) throws DataAccessException;
    public void deleteAuth(String authToken);
    void clear() throws DataAccessException;

}

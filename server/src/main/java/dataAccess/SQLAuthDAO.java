package dataAccess;

import Models.AuthTokenData;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public AuthTokenData getUser(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void oneAuthTokenPerPlayer(AuthTokenData authData) {

    }

    @Override
    public void createMemory(AuthTokenData authData) throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}

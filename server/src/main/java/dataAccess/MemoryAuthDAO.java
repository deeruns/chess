package dataAccess;

import DataAccess.DataAccessException;
import Models.AuthTokenData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String, AuthTokenData> currAuth = new HashMap<>();
    @Override
    public AuthTokenData getUser(String authToken) throws DataAccessException {
        if(!currAuth.containsKey(authToken)){
            throw new DataAccessException("Error: Unauthorized");
        }
        return currAuth.get(authToken);
    }
    @Override
    public void oneAuthTokenPerPlayer(AuthTokenData authData){
        String username = authData.username();
        for (Map.Entry<String, AuthTokenData> entry : currAuth.entrySet()) {
            AuthTokenData authTokenData = entry.getValue();
            if (authTokenData.username().equals(username)) {
                currAuth.remove(authData.authToken());
            }
        }
    }
    @Override
    public void createMemory(AuthTokenData authData) throws DataAccessException {
        if (currAuth.containsKey(authData.authToken())){
            throw new DataAccessException("Error: bad request");
        }

        currAuth.put(authData.authToken(), authData);
    }
    @Override
    public void deleteAuth(String authToken){
        currAuth.remove(authToken);
    }
    public void clear() throws DataAccessException {
        currAuth.clear();
    }

}

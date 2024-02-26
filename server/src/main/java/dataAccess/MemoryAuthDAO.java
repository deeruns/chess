package dataAccess;

import Models.AuthTokenData;
import Models.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    //CRUD operations
    private static HashMap<String, AuthTokenData> currAuth = new HashMap<>();
    //getauth
    public AuthTokenData getUser(String authToken) throws DataAccessException{
        if(!currAuth.containsKey(authToken)){
            throw new DataAccessException("Error: Unauthorized");
        }
        return currAuth.get(authToken);
    }


    public void oneAuthTokenPerPlayer(AuthTokenData authData){
//        if (currAuth.containsValue(authData.username())){
//            currAuth.remove(authData.authToken());
//            //user only has one authToken
//        }
        //if a user already has an auth, delete it and still return their new one
        String username = authData.username();
        for (Map.Entry<String, AuthTokenData> entry : currAuth.entrySet()) {
            AuthTokenData authTokenData = entry.getValue();
            if (authTokenData.username().equals(username)) {
                currAuth.remove(authData.authToken());
            }
        }
    }

    public void createMemory(AuthTokenData authData) throws DataAccessException {
        if (currAuth.containsKey(authData.authToken())){
            throw new DataAccessException("Error: bad request");
        }

        currAuth.put(authData.authToken(), authData);
    }
    public void deleteAuth(String authToken){
        currAuth.remove(authToken);
    }
    public void clear() throws DataAccessException{
        currAuth.clear();
    }

}

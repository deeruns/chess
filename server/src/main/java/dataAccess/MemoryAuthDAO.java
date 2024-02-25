package dataAccess;

import Models.AuthTokenData;
import Models.UserData;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    //CRUD operations
    private static HashMap<String, AuthTokenData> currAuth = new HashMap<>();
    //getauth
    public AuthTokenData getUser(String authToken) throws DataAccessException{
        if(!currAuth.containsKey(authToken)){
            throw new DataAccessException("invalid auth token");
        }
        return currAuth.get(authToken);
    }


    public AuthTokenData createAuthToken(UserData user){
        if (currAuth.containsKey(user.username())){
            currAuth.remove(user.username());
            //user only has one authToken
        }
        UUID auth = UUID.randomUUID();
        return new AuthTokenData(auth.toString(), user.username());
    }

    public void createMemory(AuthTokenData authData) throws DataAccessException {
        if (currAuth.containsKey(authData.authToken())){
            throw new DataAccessException("Auth token already exists");
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

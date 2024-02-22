package dataAccess;

import Models.AuthTokenData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    //CRUD operations
    private static HashMap<String, AuthTokenData> currAuth = new HashMap<>();
    //getauth
//    public AuthTokenData getUser(String authToken) throws DataAccessException{
//        if(!currAuth.containsKey(authToken)){
//            throw new DataAccessException("invalid auth token");
//        }
//        return currAuth.get(authToken);
//    }
//    //createauths
//    //removeauth
    public void clear() throws DataAccessException{
        currAuth.clear();
    }

}

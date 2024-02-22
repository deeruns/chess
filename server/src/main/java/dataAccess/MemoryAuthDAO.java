package dataAccess;

import Models.AuthTokenData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    //CRUD operations
    private static HashMap<String, AuthTokenData> currAuth = new HashMap<>();
    public void clear() throws DataAccessException{
        currAuth.clear();
    }

}

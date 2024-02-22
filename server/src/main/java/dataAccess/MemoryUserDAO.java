package dataAccess;

import Models.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private static HashMap<String, UserData> userDataArray = new HashMap<>();

    public void clear() throws DataAccessException{
        userDataArray.clear();
    }
}

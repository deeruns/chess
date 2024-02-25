package dataAccess;

import Models.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private static HashMap<String, UserData> userDataArray = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException{
        if (userDataArray.containsKey(user.username())){
            throw new DataAccessException("Error: already taken");
        }
        else{
            userDataArray.put(user.username(), user);
        }
    }
    @Override
    public UserData getUser(UserData user) throws DataAccessException{
        if (!userDataArray.containsKey(user.username())){
            throw new DataAccessException("Error: Data Access Exception");
        }
        //return userDataArray.getOrdefault(user.username(), null);
        else{
            return userDataArray.get(user.username());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        userDataArray.clear();
    }
}

package dataAccess;

import Models.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private static HashMap<String, UserData> userDataArray = new HashMap<>();


    public void create(UserData user) throws DataAccessException{
        if (userDataArray.containsKey(user.username())){
            throw new DataAccessException("Username already exists");
        }
        else{
            userDataArray.put(user.username(), user);
        }
    }

    public UserData getUser(UserData user) throws DataAccessException{
        if (!userDataArray.containsKey(user.username())){
            throw new DataAccessException("User not found");
        }
        //return userDataArray.getOrdefault(user.username(), null);
        else{
            return userDataArray.get(user.username());
        }
    }


    public void clear() throws DataAccessException{
        userDataArray.clear();
    }
}

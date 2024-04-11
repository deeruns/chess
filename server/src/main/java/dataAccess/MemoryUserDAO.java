package dataAccess;

import Models.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private static HashMap<String, UserData> userDataArray = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (userDataArray.containsKey(user.username())){
            throw new DataAccessException("Error: already taken");
        }
        else{
            userDataArray.put(user.username(), user);
        }
    }
    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (!userDataArray.containsKey(username)){
            throw new DataAccessException("Error: Unauthorized");
        }
        else{
            return userDataArray.get(username);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        userDataArray.clear();
    }
}

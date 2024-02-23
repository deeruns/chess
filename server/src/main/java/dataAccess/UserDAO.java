package dataAccess;

import Models.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(UserData user) throws DataAccessException;
}

package dataAccess;

import DataAccess.DataAccessException;
import Models.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException, DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
}

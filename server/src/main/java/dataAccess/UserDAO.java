package dataAccess;

public interface UserDAO {
//    UserDAO dao = new MemoryUserDAO();
//    public void clearCall() throws DataAccessException {
//        UserDAO daoClear = new MemoryUserDAO().clear();
//    }
    void clear() throws DataAccessException;
}

package dataAccess;

public interface GameDAO {
//    GameDAO dao = new MemoryGameDAO();
//    public void clearCall() throws DataAccessException {
//        GameDAO daoClear = new MemoryGameDAO().clear();
//    }
    void clear() throws DataAccessException;
}

package service;


import dataAccess.DataAccessException;
import dataAccess.*;

//import data from models
public class ClearService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public ClearService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        //add constructor to use these
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }
    public void clearData() throws DataAccessException {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
    }
}

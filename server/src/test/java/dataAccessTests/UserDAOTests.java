package dataAccessTests;

import Models.GameData;
import Models.UserData;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlGameDAO;
import dataAccess.SqlUserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.CreateGameService;
import service.JoinGameService;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDAOTests {
    private static SqlAuthDAO authDAO;
    private static SqlGameDAO gameDAO;
    private static SqlUserDAO userDAO;
    UserData userData = new UserData("NapoleonDynamite", "cannedheat", "decrodedPieceOfCrap@uncleRico.com");
    GameData gameOne = new GameData(1,null,null,"game", new ChessGame());
    GameData gameTwo = new GameData(2,null,null,"game", new ChessGame());
    GameData gameThree = new GameData(3,null,null,"game", new ChessGame());
    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
    }

    @BeforeAll
    public static void InitializeData() throws DataAccessException {
        authDAO = new SqlAuthDAO();
        gameDAO = new SqlGameDAO();
        userDAO = new SqlUserDAO();
        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO, userDAO);
        RegisterService registerService = new RegisterService(authDAO, userDAO);
    }

    @Test
    @DisplayName("Success Clear")
    public void ClearUsersTest() throws DataAccessException {
        userDAO.createUser(userData);
        userDAO.clear();
    }

    @Test
    @DisplayName("Success create user")
    public void CreateUsersTest() throws DataAccessException {
        userDAO.createUser(userData);
    }
    @Test
    @DisplayName("Fail create user")
    public void CreateUsersTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(userData);
            userDAO.createUser(userData);
        });
    }

    @Test
    @DisplayName("Success getUser")
    public void getUsersTest() throws DataAccessException {
        userDAO.createUser(userData);
        userDAO.getUser(userData.username());
    }

    @Test
    @DisplayName("Fail get user")
    public void getUsersTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userDAO.getUser(userData.username());
        });
    }

}

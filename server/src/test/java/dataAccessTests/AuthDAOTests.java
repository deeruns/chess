package dataAccessTests;

import Models.AuthTokenData;
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

public class AuthDAOTests {
    private static SqlAuthDAO authDAO;
    private static SqlGameDAO gameDAO;
    private static SqlUserDAO userDAO;
    UserData userData = new UserData("NapoleonDynamite", "cannedheat", "decrodedPieceOfCrap@uncleRico.com");
    AuthTokenData authTokenData = new AuthTokenData("12345", "napoleon");
    AuthTokenData authTokenData2 = new AuthTokenData("1234567", "kip");
    AuthTokenData authTokenData3 = new AuthTokenData("12345", "nap");
    GameData gameOne = new GameData(1,null,null,"game", new ChessGame());
    GameData gameTwo = new GameData(2,null,null,"game", new ChessGame());
    GameData gameThree = new GameData(3,null,null,"game", new ChessGame());
    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO.clear();
    }

    @BeforeAll
    public static void InitializeData() throws DataAccessException {
        authDAO = new SqlAuthDAO();
        gameDAO = new SqlGameDAO();
        userDAO = new SqlUserDAO();
    }

    @Test
    @DisplayName("Success Clear")
    public void clearAuthTest() throws DataAccessException {
        authDAO.createMemory(authTokenData);
        authDAO.clear();
    }
    @Test
    @DisplayName("Success delete auth")
    public void deleteAuthTest() throws DataAccessException {
        authDAO.createMemory(authTokenData);
        authDAO.deleteAuth(authTokenData.authToken());
    }
    @Test
    @DisplayName("Fail delete auth")
    public void deleteAuthTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.createMemory(authTokenData);
            authDAO.deleteAuth(authTokenData2.authToken());
        });
    }
    @Test
    @DisplayName("Success create auth")
    public void createAuthTest() throws DataAccessException {
        authDAO.createMemory(authTokenData);
    }
    @Test
    @DisplayName("Fail delete auth")
    public void createAuthTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.createMemory(authTokenData);
            authDAO.createMemory(authTokenData3);
        });
    }
    @Test
    @DisplayName("Success get auth")
    public void getAuthTest() throws DataAccessException {
        authDAO.createMemory(authTokenData);
        authDAO.getUser(authTokenData.authToken());
    }
    @Test
    @DisplayName("Fail get auth")
    public void getAuthTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.createMemory(authTokenData);
            authDAO.getUser(authTokenData2.authToken());
        });
    }


}

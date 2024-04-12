package ServiceUnitTests;

import dataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.GameData;
import Models.UserData;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.*;
import service.ListGamesService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
    private static JoinGameService joinGameService;
    private static service.ListGamesService listGamesService;
    private static LoginService loginService;
    private static LogoutService logoutService;
    private static RegisterService registerService;
    private static MemoryAuthDAO authDAO;
    private static MemoryGameDAO gameDAO;
    private static MemoryUserDAO userDAO;
    private static UserData userData;
    private static GameData gameData;
    private static AuthTokenData authTokenData;

    @BeforeAll
    public static void InitializeData() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
        clearService = new ClearService(authDAO,gameDAO,userDAO);
        createGameService = new CreateGameService(authDAO, gameDAO);
        joinGameService = new JoinGameService(authDAO, gameDAO, userDAO);
        listGamesService = new ListGamesService(authDAO, gameDAO);
        loginService = new LoginService(authDAO, userDAO);
        logoutService = new LogoutService(authDAO);
        registerService = new RegisterService(authDAO, userDAO);
        userData = new UserData("NapoleonDynamite","cannedheat","decrodedPieceOfCrap@uncleRico.com");

    }

    @BeforeEach
    public void resetBeforeTest() throws DataAccessException {
        try {
            clearService.clearData();
            authTokenData = registerService.registerUser(userData.username(), userData.password(), userData.email());
        }
        catch (DataAccessException exception) {
            throw new DataAccessException("Clear Failed");
        }
    }

    @Test
    @DisplayName("Success Login")
    public void LoginUser(){
        Assertions.assertDoesNotThrow(() -> loginService.loginUser(userData.username(), userData.password()));
    }
    @Test
    @DisplayName("Login: Wrong Password")
    public void LoginUserWrongPassword() throws DataAccessException{
        String expectedOutput = "Error: Unauthorized";
        String wrongPassword = "YouCouldBeDrinkingWholeMilkIfYouWanted";
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> loginService.loginUser(userData.username(), wrongPassword));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
}

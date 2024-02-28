package ServiceUnitTests;

import Models.AuthTokenData;
import Models.GameData;
import Models.UserData;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.*;
import service.ListGamesService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogoutTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
    private static LogoutService logoutService;
    private static RegisterService registerService;
    private static MemoryAuthDAO authDAO;
    private static UserData userData;
    private static GameData gameData;
    private static AuthTokenData authTokenData;

    @BeforeAll
    public static void InitializeData() {
        authDAO = new MemoryAuthDAO();
        logoutService = new LogoutService(authDAO);
        userData = new UserData("NapoleonDynamite","cannedheat","decrodedPieceOfCrap@uncleRico.com");
        //authWrong = null;
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
    @DisplayName("Success Logout")
    public void LogoutUser(){
        String auth = authTokenData.authToken();
        Assertions.assertDoesNotThrow(() -> logoutService.logoutUser(auth));
    }
    @Test
    @DisplayName("Unauthorized Logout")
    public void LogoutUserUnauthorized() throws DataAccessException{
        String expectedOutput = "Error: Unauthorized";
        String wrongAuth = "TinaYouFatLard";
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> logoutService.logoutUser(wrongAuth));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
}

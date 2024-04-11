package ServiceUnitTests;

import Models.AuthTokenData;
import Models.GameData;
import Models.UserData;
import dataAccess.*;
import org.junit.jupiter.api.*;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
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
            throw new DataAccessException("Failed to clear the DB");
        }

        try {
            gameData = createGameService.createGame(authTokenData.authToken(), "ilovetechnology");
        }
        catch (DataAccessException exception){
            System.out.println(exception.getMessage());
        }

    }

    @Test
    @DisplayName("Success Register")
    public void RegisterNewUser(){
        UserData newuser = new UserData("kip", "lafawnda", "rexkwondo@gmail.com");
        Assertions.assertDoesNotThrow(() -> registerService.registerUser(newuser.username(), newuser.password(), newuser.email()));
    }
    @Test
    @DisplayName("Failed Register: Name Taken")
    public void RegisterNameTaken() throws DataAccessException {
        String expectedOutput = "Error: already taken";
        //Assertions.assertDoesNotThrow(() -> registerService.registerUser(userData.username(), userData.password(), userData.email()));
        DataAccessException exception = assertThrows(DataAccessException.class, ()->registerService.registerUser(userData.username(), userData.password(), userData.email()));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }

}

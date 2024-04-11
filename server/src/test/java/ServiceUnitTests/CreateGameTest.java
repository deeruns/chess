package ServiceUnitTests;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.GameData;
import Models.UserData;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateGameTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
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
    @DisplayName("Success CreateGame")
    public void CreateGameTestSuccess(){
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(authTokenData.authToken(), "GiveMeSomeOfYourTots"));
    }
    @Test
    @DisplayName("CreateGame: Unauthorized")
    public void CreateGameUnauthorized() throws DataAccessException{
        String expectedOutput = "Error: Unauthorized";
        String wrongAuth = "NoGoFindYourOwn";
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> createGameService.createGame(wrongAuth, "UGHHH"));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
    @Test
    @DisplayName("CreateGame: bad request")
    public void CreateGameNameTaken() throws DataAccessException{
        String expectedOutput = "Error: bad request";
        String gameName = "BuildHerACake";
        //create a game to already exist
        gameData = createGameService.createGame(authTokenData.authToken(), gameName);
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> createGameService.createGame(authTokenData.authToken(), gameName));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }

}

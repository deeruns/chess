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

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JoinGameTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
    private static JoinGameService joinGameService;
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
    @DisplayName("Success JoinGame")
    public void JoinGameTestSuccess(){
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame("WHITE", gameData.gameID(), authTokenData.authToken()));
    }
    @Test
    @DisplayName("JoinGame: Wrong GameID")
    public void CreateGameUnauthorized() throws DataAccessException{
        String expectedOutput = "Error: bad request";
        int wrongID = 1234;
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> joinGameService.joinGame("WHITE", wrongID, authTokenData.authToken()));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
    @Test
    @DisplayName("JoinGame: color taken")
    public void CreateGameNameTaken() throws DataAccessException{
        String expectedOutput = "Error: already taken";
        joinGameService.joinGame("WHITE", gameData.gameID(), authTokenData.authToken());
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> joinGameService.joinGame("WHITE", gameData.gameID(), authTokenData.authToken()));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
}

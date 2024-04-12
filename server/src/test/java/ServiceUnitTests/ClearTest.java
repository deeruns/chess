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

public class ClearTest {
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
        //authWrong = null;
    }

    @BeforeEach
    public void resetBeforeTest() throws DataAccessException {
        try {
            clearService.clearData();
            authTokenData = registerService.registerUser(userData.username(), userData.password(), userData.email());
            authTokenData = registerService.registerUser("UncleRico", "SoakingItUpInAHotTubWithMySoulMate", "football@gmail.com");
        }
        catch (DataAccessException exception) {
            throw new DataAccessException("Clear Failed");
        }

        try {
            gameData = createGameService.createGame(authTokenData.authToken(), "ilovetechnology");
            gameData = createGameService.createGame(authTokenData.authToken(), "fuhgetaboutit");
        }
        catch (DataAccessException exception){
            System.out.println(exception.getMessage());
        }
    }
    @Test
    @DisplayName("Success Clear")
    public void ClearDataTest(){
        Assertions.assertDoesNotThrow(() -> clearService.clearData());
    }
}

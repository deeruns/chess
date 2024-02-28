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

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListGamesTest {
    private static ClearService clearService;
    private static CreateGameService createGameService;
    private static service.ListGamesService listGamesService;
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
        listGamesService = new service.ListGamesService(authDAO, gameDAO);
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

        try {
            gameData = createGameService.createGame(authTokenData.authToken(), "ilovetechnology");
            gameData = createGameService.createGame(authTokenData.authToken(), "tinaYouFatLard");
        }
        catch (DataAccessException exception){
            System.out.println(exception.getMessage());
        }

    }
    @Test
    @DisplayName("Success ListGames")
    public void JoinGameTestSuccess(){
        Assertions.assertDoesNotThrow(() -> listGamesService.listGames(authTokenData.authToken()));
    }
    @Test
    @DisplayName("ListGame: Unauthorized")
    public void CreateGameUnauthorized() throws DataAccessException{
        String expectedOutput = "Error: Unauthorized";
        String wrongauth = "WhateverIFeelLikeGoshhhh";
        DataAccessException exception = assertThrows(DataAccessException.class, ()-> listGamesService.listGames(wrongauth));
        Assertions.assertEquals(expectedOutput, exception.getMessage());
    }
}

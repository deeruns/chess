package dataAccessTests;

import Models.GameData;
import Models.UserData;
import chess.ChessGame;
import dataAccess.*;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.CreateGameService;
import service.JoinGameService;
import service.RegisterService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameDAOTests {
    private static SqlAuthDAO authDAO;
    private static SqlGameDAO gameDAO;
    private static SqlUserDAO userDAO;
    UserData userData = new UserData("NapoleonDynamite", "cannedheat", "decrodedPieceOfCrap@uncleRico.com");
    GameData gameOne = new GameData(1,null,null,"game", new ChessGame());
    GameData gameTwo = new GameData(2,null,null,"game", new ChessGame());
    GameData gameThree = new GameData(3,null,null,"game", new ChessGame());
    @BeforeEach
    public void setup() throws DataAccessException{
        gameDAO.clear();
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
    public void ClearGamesTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.clear();
    }

    @Test
    @DisplayName("Success Join Game")
    public void JoinGameTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.addUser(gameOne.gameID(), userData.username(), "WHITE");
    }

    @Test
    @DisplayName("Success Create Game")
    public void CreateGameTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
    }

    @Test
    @DisplayName("Success Get Game Name")
    public void getGameNameTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.getGameName(gameOne.gameName());
    }

    @Test
    @DisplayName("Success Get Game")
    public void getGameTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.getGame(gameOne.gameID());
    }

    @Test
    @DisplayName("Success List Game")
    public void ListGameTest() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.createGame(gameTwo.gameName());
        gameDAO.createGame(gameThree.gameName());
        Collection<GameData> gameList = gameDAO.listGames();
        Collection<GameData> otherList = new ArrayList<>();
        otherList.add(gameOne);
        otherList.add(gameTwo);
        otherList.add(gameThree);
        Assertions.assertEquals(gameList.size(), otherList.size());
    }

    @Test
    @DisplayName("Fail Join Game")
    public void JoinGameTestFail() throws DataAccessException {
        gameDAO.createGame(gameOne.gameName());
        gameDAO.addUser(gameOne.gameID(), userData.username(), "WHITE");
    }

    @Test
    @DisplayName("Fail Create Game")
    public void CreateGameTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(gameOne.gameName());
            gameDAO.createGame(gameOne.gameName());
            gameDAO.createGame(gameOne.gameName());
        });
    }

    @Test
    @DisplayName("Fail Get Game Name")
    public void getGameNameTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.clear();
            gameDAO.getGameName(gameOne.gameName());
        });
    }

    @Test
    @DisplayName("Fail Get Game")
    public void getGameTestFail() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(gameOne.gameID());
        });
    }

    @Test
    @DisplayName("Fail List Game")
    public void ListGameTestFail() throws DataAccessException {
        Collection<GameData> gameList = gameDAO.listGames();
        Collection<GameData> otherList = new ArrayList<>();
        Assertions.assertEquals(gameList, otherList);
    }


}

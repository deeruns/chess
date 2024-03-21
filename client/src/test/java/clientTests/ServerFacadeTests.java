package clientTests;

import Models.AuthTokenData;
import dataAccess.DataAccessException;
import dataAccess.SqlGameDAO;
import org.junit.jupiter.api.*;
import requests.*;
import response.CreateGameResponse;
import response.ListGamesResponse;
import server.Server;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
//        server = new Server();
//        var port = server.run(8080);
//        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

//    @AfterAll
//    static void stopServer() {
//        server.stop();
//    }
    @AfterAll
    static void clearDB() throws DataAccessException {
        facade.clear();
    }
    @BeforeAll
    public static void clear() throws DataAccessException {
        facade.clear();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }
    @Test
    @DisplayName("Success Register")
    void registerSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("napoleon", "dynamite", "asdfiauifh");
        var authData = facade.register(request);
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    @DisplayName("Fail Register")
    void registerFail() throws Exception {
        RegisterRequest request = new RegisterRequest("napoleon", "dynamite", "asdfiauifh");
        //DataAccessException exception = Assertions.assertThrows(facade.register(request));
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.register(request);
        });

    }

    @Test
    @DisplayName("Success Login")
    void LoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    @DisplayName("Fail Login")
    void loginFail() throws Exception {
        LoginRequest request = new LoginRequest("wronguser", "bruh");
        //DataAccessException exception = Assertions.assertThrows(facade.register(request));
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.login(request);
        });

    }

    @Test
    @DisplayName("Success logout")
    void logoutSuccess() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        LogoutRequest request2 = new LogoutRequest(authData.authToken());
        facade.logout(request2);
    }

    @Test
    @DisplayName("Fail logout")
    void logoutFail() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        LogoutRequest request2 = new LogoutRequest("notanauth");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.logout(request2);
        });
    }
    @Test
    @DisplayName("Success createGame")
    void createGamesSuccess() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        CreateGameRequest request2 = new CreateGameRequest("testgame",authData.authToken());
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.createGame(request2);
        });
    }
    @Test
    @DisplayName("Fail createGame")
    void createGamesFail() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        //game already exists
        CreateGameRequest request2 = new CreateGameRequest("testgame",authData.authToken());
        CreateGameResponse response = facade.createGame(request2);
        assertTrue(response.gameID() > 0);

    }


    @Test
    @DisplayName("Success listGames")
    void listGamesSuccess() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        ListGamesRequest request2 = new ListGamesRequest(authData.authToken());
        ListGamesResponse response = facade.listGames(request2);
        assertNotNull(response);
    }
    @Test
    @DisplayName("Fail listGames")
    void listGamesFail() throws Exception {
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        ListGamesRequest request2 = new ListGamesRequest("notanauth");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.listGames(request2);
        });

    }

    @Test
    @DisplayName("Success joinGame")
    void joinGameSuccess() throws Exception {
        SqlGameDAO sqlGameDAO = new SqlGameDAO();
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        JoinGameRequest request2 = new JoinGameRequest("WHITE", 1, authData.authToken());
        facade.joinGame(request2);
        assertEquals("napoleon",sqlGameDAO.getGame(1).whiteUsername());

    }

    @Test
    @DisplayName("fail join game")
    void joinGameFail() throws Exception {
        SqlGameDAO sqlGameDAO = new SqlGameDAO();
        LoginRequest request = new LoginRequest("napoleon", "dynamite");
        var authData = facade.login(request);
        JoinGameRequest request2 = new JoinGameRequest("WHITE", 12788, authData.authToken());
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            facade.joinGame(request2);
        });

    }


}

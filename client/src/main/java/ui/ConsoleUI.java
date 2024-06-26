package ui;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.GameData;
import WebSocket.NotificationHandler;
import WebSocket.WebSocketFacade;
import chess.ChessGame;
import com.google.gson.Gson;
import requests.*;
import response.CreateGameResponse;
import response.ListGamesResponse;
import serverFacade.ServerFacade;
import webSocketMessages.serverMessages.ErrorCommand;
import webSocketMessages.serverMessages.LoadGameCommand;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.NotificationCommand;


import java.util.Objects;
import java.util.Scanner;

//import static clientTests.ServerFacadeTests.server;
import static java.lang.System.out;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;


public class ConsoleUI implements NotificationHandler {
    //NotificationHandler notificationHandler;
    private String authToken = null;
    //make a chessgame variable and reset the board there?
    private final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    private final WebSocketFacade ws;
    UserLoginStatus status = UserLoginStatus.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    GamePlayUI gamePlayUI;
    ChessGame chessGame;
    ChessGame.TeamColor teamColor;


    public ConsoleUI() throws DataAccessException {

        this.ws = new WebSocketFacade("http://localhost:8080", this);
    }

    @Override
    public void notify(String notification, ServerMessage.ServerMessageType type) {

        System.out.println("\n");
        switch (type) {
            case LOAD_GAME: {
                LoadGameCommand message = new Gson().fromJson(notification, LoadGameCommand.class);
                DrawChessBoard.drawChessBoard(teamColor, message.getGame(), null);
                break;
            }
            case ERROR: {
                ErrorCommand errorCommand = new Gson().fromJson(notification, ErrorCommand.class);
                String errorMessage = errorCommand.getErrorMessage();
                System.out.println(SET_TEXT_COLOR_RED + errorMessage); //get actual error
                break;
            }
            case NOTIFICATION:{
                NotificationCommand notificationCommand = new Gson().fromJson(notification, NotificationCommand.class);
                System.out.println(SET_TEXT_COLOR_MAGENTA + notificationCommand.getMessage());
                break;
            }
            default: {
                System.out.println("error: in notify");
                break;
            }
        }
    }


    public String evalInput(String input){
        try{
            return switch (input){
                case "1" -> clientLogin();
                case "2" -> clientRegister();
                case "9" -> clientLogout();
                case "6" -> clientCreateGame();
                case "7" -> clientJoinGame();
                case "8" -> clientObserveGame();
                case "5" -> clientListGame();
                case "10" -> clear();
                case "3" -> ";)";
                case "4" -> "Enter 1 to login, Enter 2 to register, Enter 3 to quit, Enter 4 for help. After logging in, Enter 5 to list all games, Enter 6 to create a new game, Enter 7 to Join a game, Enter 8 to join as an observer with your color set as \"\", Enter 9 to logout, and Enter 10 to clear";
                default -> "not very cash money, your input is invalid brah";
            };
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    public String clientHelp() {
        if (status == UserLoginStatus.SIGNEDOUT) {
            return """
                    Enter the Number of the action you wish to take:
                    1. Login
                    2. Register
                    3. quit
                    4. Help
                    """;
        }
        return """
                Enter the Number of the action you wish to take:
                3. quit
                4. Help
                5. List Games
                6. Create Game
                7. Join Game
                8. Join Observer
                9. Log Out
                10. Clear
                """;
    }

    private String clear() throws DataAccessException {
        try{
            serverFacade.clear();
            //out.println(clientHelp());
            return "Database has been cleared";
        } catch (DataAccessException exception) {
            return exception.getMessage();
        }
    }

    private String clientRegister() throws DataAccessException {
        try{
            out.print("Username: ");
            String username = scanner.next();
            out.print("Password: ");
            String password = scanner.next();
            out.print("Email: ");
            String email = scanner.next();
            AuthTokenData authTokenData = serverFacade.register(new RegisterRequest(username, password, email));
            authToken = authTokenData.authToken();
            status = UserLoginStatus.SIGNEDIN;
            //System.out.println(clientHelp());
            return "Successful Registered " + username;

        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }

    }

    private String clientLogout() throws DataAccessException {
        try{
            //authorize
            serverFacade.logout(new LogoutRequest(authToken));
            status = UserLoginStatus.SIGNEDOUT;
            authToken = null;
            //out.println(clientHelp());
            return "Successful Logout";
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
    }

    private String clientCreateGame() throws DataAccessException {
        try{
            out.print("Enter Game name: ");
            String gameName = scanner.next();
            CreateGameResponse response = serverFacade.createGame(new CreateGameRequest(gameName, authToken));
            getGame(response.gameID());
            resetnewboard(response.gameID());
            return "Game " + gameName + " Created Successfully";
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
    }

    private String clientJoinGame() throws DataAccessException {
        try{
            out.print("Enter GameID: ");
            int gameID = Integer.parseInt(scanner.next());
            out.print("Enter team color WHITE or BLACK: ");
            String color = scanner.next().toLowerCase();

            serverFacade.joinGame(new JoinGameRequest(color.toUpperCase(), gameID, authToken));
            getGame(gameID);
            gamePlayUI = new GamePlayUI(ws,authToken, chessGame);
            gamePlayUI.setGameID(gameID);
            if (color.equals("white")){
                gamePlayUI.setObserve(false);
                teamColor = ChessGame.TeamColor.WHITE;
                gamePlayUI.setTeamColor(teamColor);
                gamePlayUI.joinGamePlay(teamColor);
                System.out.println(SET_TEXT_COLOR_GREEN + "Successfully joined game as WHITE" + SET_TEXT_COLOR_WHITE);
                gamePlayUI.evalInput();
            }
            else if (color.equals("black")){
                gamePlayUI.setObserve(false);
                teamColor = ChessGame.TeamColor.BLACK;
                gamePlayUI.setTeamColor(teamColor);
                gamePlayUI.joinGamePlay(teamColor);
                System.out.println(SET_TEXT_COLOR_GREEN + "Successfully joined game as BLACK" + SET_TEXT_COLOR_WHITE);
                gamePlayUI.evalInput();
            }
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
        return "";
    }

    private String clientObserveGame() throws DataAccessException {
        try{
            out.print("Enter GameID: ");
            int gameID = Integer.parseInt(scanner.next());
            teamColor = null;
            serverFacade.joinGame(new JoinGameRequest("", gameID, authToken));
            getGame(gameID);
            gamePlayUI = new GamePlayUI(ws,authToken, chessGame);
            gamePlayUI.setGameID(gameID);
            gamePlayUI.setObserve(true);
            gamePlayUI.setTeamColor(teamColor);
            gamePlayUI.joinGamePlay(teamColor);
            System.out.println(SET_TEXT_COLOR_GREEN + "Successfully observing game" + SET_TEXT_COLOR_WHITE);
            gamePlayUI.evalInput();
            return "";
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
    }

    private String clientListGame() throws DataAccessException {
        try{
            //authorize
            ListGamesResponse response = serverFacade.listGames(new ListGamesRequest(authToken));
            for (GameData game: response.games()){
                out.println("Game Name: " + game.gameName() + ", White: " + game.whiteUsername() + ", Black: " + game.blackUsername() + ", Game ID: " + game.gameID());
            }
        }
        catch (DataAccessException exception){
            out.println(exception.getMessage());
        }
        return "";
    }

    private String clientLogin() throws DataAccessException {
        try{
            out.print("Username: ");
            String username = scanner.next();
            out.print("Password: ");
            String password = scanner.next();
            AuthTokenData authTokenData = serverFacade.login( new LoginRequest(username, password));
            authToken = authTokenData.authToken();
            status = UserLoginStatus.SIGNEDIN;
            return "Welcome " + username;
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }

    }
    ChessGame getGame(int gameID) throws DataAccessException {
        ListGamesRequest req = new ListGamesRequest(authToken);
        ListGamesResponse response = serverFacade.listGames(req);
        for (GameData gameData : response.games()) {
            if (Objects.equals(gameData.gameID(), gameID)) {
                chessGame = gameData.game();
                return chessGame;
            }
        }
        return null;
    }
    void resetnewboard(int gameID) throws DataAccessException {
        ListGamesRequest req = new ListGamesRequest(authToken);
        ListGamesResponse response = serverFacade.listGames(req);
        for (GameData gameData : response.games()) {
            if (Objects.equals(gameData.gameID(), gameID)) {
                gameData.game().getBoard().resetBoard();
                chessGame = gameData.game();

            }
        }
    }


}

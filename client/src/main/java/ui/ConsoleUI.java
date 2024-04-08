package ui;

import Models.AuthTokenData;
import Models.GameData;
import ResponseException.ResponseException;
import WebSocket.NotificationHandler;
import WebSocket.WebSocketFacade;
import dataAccess.DataAccessException;
import requests.*;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.ResponseRecord;
import serverFacade.ServerFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;
import static java.lang.System.out;


public class ConsoleUI implements NotificationHandler {
    NotificationHandler notificationHandler;
    private String authToken = null;
    private final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    private final WebSocketFacade ws;
    UserLoginStatus status = UserLoginStatus.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    GamePlayUI gamePlayUI = new GamePlayUI();

    public ConsoleUI() throws DataAccessException {
        this.ws = new WebSocketFacade("http://localhost:8080", notificationHandler);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
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

    private String clientLogout() throws DataAccessException{
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
            //authorize?
            CreateGameResponse response = serverFacade.createGame(new CreateGameRequest(gameName, authToken));
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
            //authorize
            serverFacade.joinGame(new JoinGameRequest(color.toUpperCase(), gameID, authToken));
            //gameplayUI
            //gamePlayUI.evalInput(scanner.next());
            //insert player color
            //DrawChessBoard.drawChessBoard();
            //return "Succcessfully Joined Game " + gameID + "as " + color;
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
        return "Succcessfully Joined Game";
    }

    private String clientObserveGame() throws DataAccessException {
        try{
            out.print("Enter GameID: ");
            int gameID = Integer.parseInt(scanner.next());
            out.print("Enter team color WHITE or BLACK: ");
            String color = scanner.next();
            //authorize
            serverFacade.joinGame(new JoinGameRequest(color, gameID, authToken));
            //insert player color
            //DrawChessBoard.drawChessBoard();
            return "Succcessfully Joined Game " + gameID + "as an Observer";
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }
    }

    private String clientListGame() throws DataAccessException{
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
            //System.out.println(clientHelp());
            return "Welcome " + username;
        }
        catch(DataAccessException exception){
            return exception.getMessage();
        }

    }

    private void notify(ServerMessage message){}

    //observer board displayed as white
    // make new UI for gameplay

}

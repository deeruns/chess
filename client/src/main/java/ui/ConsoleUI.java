package ui;

import Models.AuthTokenData;
import ResponseException.ResponseException;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.ResponseRecord;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.getSecurityManager;
import static java.lang.System.out;


public class ConsoleUI {
    private String authToken = null;
    private final ServerFacade serverFacade = new ServerFacade("http://localhost:8080");
    UserLoginStatus status = UserLoginStatus.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    DrawChessBoard DrawBoard = new DrawChessBoard();
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
                case "3" -> "Go eat a decroded piece of crap";
                case "4" -> clientHelp();
                default -> "not very cash money, your input is invalid brah";
            };
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String clientHelp() {
        if (status == UserLoginStatus.SIGNEDOUT) {
            return """
                    Enter the Number of the action you wish to take:
                    1. Login <username, password>
                    2. Register <username, password, email>
                    3. quit
                    4. Help
                    """;
        }
        return """
                Enter the Number of the action you wish to take:
                4. Help
                5. List Games
                6. Create Game
                7. Join Game
                8. Join Observer
                9. Log Out
                10. Clear
                """;
    }

    private String clear() {

    }

    private String clientRegister() throws ResponseException {
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
            return "Successful Register";
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }

    }

    private String clientLogout() throws ResponseException{
        try{
            //authorize
            ResponseRecord response = serverFacade.logout(request);
            status = UserLoginStatus.SIGNEDOUT;
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }
    }

    private String clientCreateGame() throws ResponseException {
        try{
            out.print("Enter Game name: ");
            String gameName = scanner.next();
            //authorize?
            CreateGameResponse response = serverFacade.createGame(new CreateGameRequest(gameName));
            return "Game Created Successfully";
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }
    }

    private String clientJoinGame() throws ResponseException {
        try{
            out.print("Enter GameID: ");
            int gameID = Integer.parseInt(scanner.next());
            out.print("Enter team color WHITE or BLACK: ");
            String color = scanner.next();
            //authorize
            ResponseRecord response = serverFacade.joinGame(new JoinGameRequest(color, gameID));
            return "Succcessfully Joined Game " + gameID + "as " + color;
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }
    }

    private String clientObserveGame() throws ResponseException {
        try{
            out.print("Enter GameID: ");
            int gameID = Integer.parseInt(scanner.next());
            out.print("Enter team color WHITE or BLACK: ");
            String color = scanner.next();
            //authorize
            ResponseRecord response = serverFacade.joinGame(new JoinGameRequest(color, gameID));
            return "Succcessfully Joined Game " + gameID + "as an Observer";
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }
    }

    private String clientListGame() throws ResponseException{
        try{
            //authorize
            ListGamesResponse response = serverFacade.listGames();
        }
        catch (ResponseException exception){
            return exception.getMessage();
        }
    }

    private String clientLogin() throws ResponseException {
        try{
            out.print("Username: ");
            String username = scanner.next();
            out.print("Password: ");
            String password = scanner.next();
            AuthTokenData authTokenData = serverFacade.login( new LoginRequest(username, password));
            authToken = authTokenData.authToken();
            status = UserLoginStatus.SIGNEDIN;
            return "Successful Login";
        }
        catch(ResponseException exception){
            return exception.getMessage();
        }

    }
}

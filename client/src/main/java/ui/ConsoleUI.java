package ui;

import ResponseException.ResponseException;

import java.util.Arrays;
import java.util.Scanner;


public class ConsoleUI {
    UserLoginStatus status = UserLoginStatus.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    DrawChessBoard DrawBoard = new DrawChessBoard();
    public String evalInput(String input){
        try{
            var inputTokens = input.toLowerCase().split(" ");
            var cmd = (inputTokens.length > 0) ? inputTokens[0] : "help";
            var inputParams = Arrays.copyOfRange(inputTokens, 1, inputTokens.length);
            return switch (cmd){
                case "login" -> clientLogin(inputParams);
                case "register" -> clientRegister(inputParams);
                case "logout" -> clientLogout();
                case "create game" -> clientCreateGame();
                case "join game" -> clientJoinGame();
                case "observe game" -> clientObserveGame();
                case "list games" -> clientListGame();
                case "clear" -> clear();
                case "quit" -> "Go eat a decroded piece of crap";
                case "help" -> clientHelp();
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
                    1. Sign in <username, password>
                    2. Register <username, password, email>
                    3. quit
                    4. Help
                    """;
        }
        return """
                1. List Games
                2. Create Game
                3. Join Game
                4. Join Observer
                5. Log Out
                6. Help
                """;
    }

    private String clear() {

    }

    private String clientRegister(String[] inputParams) {
    }

    private String clientLogout() {
    }

    private String clientCreateGame() {
    }

    private String clientJoinGame() {
    }

    private String clientObserveGame() {
    }

    private String clientListGame() {
    }

    private String clientLogin(String[] inputParams) throws ResponseException {
        if (inputParams.length > 2){
            throw new ResponseException(400, "too many arguments");
        }
        else if (inputParams.length < 2){
            throw new ResponseException(400, "enter username and password");
        }

    }
}

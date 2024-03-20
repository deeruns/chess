package ui;

import ResponseException.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.out;


public class ConsoleUI {
    private String authToken = null;
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

    private String clientRegister() {
        out.print("Username: ");
        String username = scanner.next();
        out.print("Password: ");
        String password = scanner.next();
        out.print("Email: ");
        String email = scanner.next();
    }

    private String clientLogout() {
    }

    private String clientCreateGame() {
        out.print("Enter Game name: ");
    }

    private String clientJoinGame() {
        out.print("Enter GameID: ");
        int gameID = Integer.parseInt(scanner.next());
        out.print("Enter team color WHITE or BLACK: ");
        String username = scanner.next();
    }

    private String clientObserveGame() {
        out.print("Enter GameID: ");
        int gameID = Integer.parseInt(scanner.next());
    }

    private String clientListGame() {
    }

    private String clientLogin() throws ResponseException {
        out.print("Username: ");
        String username = scanner.next();
        out.print("Password: ");
        String password = scanner.next();


    }
}

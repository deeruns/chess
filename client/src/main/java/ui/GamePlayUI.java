package ui;

import ResponseException.ResponseException;
import dataAccess.DataAccessException;

import java.util.Scanner;

import static java.lang.System.out;

public class GamePlayUI {
    Scanner scanner = new Scanner(System.in);

    public String evalInput(String input){
        try{
            return switch (input){
                case "1" -> GamePlayHelp();
                case "2" -> redrawChessBoard();
                case "3" -> leave();
                case "4" -> makeMove();
                case "5" -> resign();
                case "6" -> highlightMoves();
                default -> "not very cash money, your input is invalid brah";
            };
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String printMenu() {
        return """
                Enter the number of the action you want to take
                1. Help
                2. Redraw Chess Board
                3. Leave Game
                4. Make a Move
                5. Resign
                6. Highlight Possible Moves
                """;
    }
    private String highlightMoves() {
        try{
            out.println("Enter the position\n" + "of the piece you would like to highlight: ");
            String position = scanner.next();
            //draw chess board highlighted
        }
        catch (DataAccessException exception){
            return exception.getMessage();
        }
        return "";
    }

    private String resign() {
        return "";
    }

    private String makeMove() {
        out.println("");
        return "";
    }

    private String leave() {
        return "";
    }

    private String redrawChessBoard() {
        //print according to the team they are on
        DrawChessBoard.drawChessBoard();
        return "";
    }

    private String GamePlayHelp() {
        return """
                Enter the number of the action you want to take.
                Enter 1 for help
                Enter 2 to draw the Chess Board
                Enter 3 to leave the game
                Enter 4 to move a piece on the chess board. Enter the Coordinates you want to move to ex: b4
                Enter 5 resign from the game
                Enter 6 to highlight the possible moves for a piece
                """;
    }

    private String evalPosition(String input) {
        return switch (input){
            case "1" -> GamePlayHelp();
            case "2" -> redrawChessBoard();
            case "3" -> leave();
            case "4" -> makeMove();
            case "5" -> resign();
            case "6" -> highlightMoves();
            default -> "not very cash money, your input is invalid brah";
    }
}

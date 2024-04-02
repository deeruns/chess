package ui;

import ResponseException.ResponseException;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

public class GamePlayUI {
    Scanner scanner = new Scanner(System.in);

    public String evalInput(String input) {
        try {
            return switch (input) {
                case "1" -> GamePlayHelp();
                case "2" -> redrawChessBoard();
                case "3" -> leave();
                case "4" -> makeMove();
                case "5" -> resign();
                case "6" -> highlightMoves();
                default -> "not very cash money, your input is invalid brah";
            };
        } catch (Exception exception) {
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
        out.println("Enter the position\n" + "of the piece you would like to highlight: ");
        String position = scanner.next();
        ArrayList<Integer> cords = evalPosition(position);
        //call highlight moves with cords
        //draw chess board highlighted
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
        //DrawChessBoard.drawChessBoard();
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

    private ArrayList<Integer> evalPosition(String input) {
        //make sure this initialization isn't passed in and it is changed when going through the loop to an actual coordinate number
        int numTwoPos = 0;
        String chars = null;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numTwoPos = Integer.valueOf(c);
            } else {
                chars = String.valueOf(c);
            }
        }
        assert chars != null;
        int numOnePos = Integer.parseInt(evalNumPos(chars));
        ArrayList<Integer> cords = new ArrayList<>();
        cords.add(numOnePos);
        cords.add(numTwoPos);
        //returns an array of two integers (x,y) or (row, col)
        return cords;
    }

    private String evalNumPos(String input){
        return switch (input) {
            //may need to change these to indexing numbers? -1
            case "a" -> "1";
            case "b" -> "2";
            case "c" -> "3";
            case "d" -> "4";
            case "e" -> "5";
            case "f" -> "6";
            case "g" -> "7";
            case "h" -> "8";
            default -> "Invalid";
        };
    }
};

package ui;

import ResponseException.ResponseException;
import chess.*;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static java.lang.System.out;

public class GamePlayUI {
    Scanner scanner = new Scanner(System.in);
    ChessGame.TeamColor teamColor;
    ChessGame game;
    ChessBoard board;
    String authToken;
    ChessPosition cords;

    public GamePlayUI(String auth, ChessGame gameChess) {
        game = gameChess;
        board = game.getBoard();
        board.resetBoard();
        authToken = auth;
    }


    public void evalInput(String var) {
        out.println(printMenu(var));
        String input = scanner.nextLine();
        try {
            switch (input) {
                case "1" -> GamePlayHelp(var);
                case "2" -> redrawChessBoard(var);
                case "3" -> leave();
                case "4" -> makeMove(var);
                case "5" -> resign(var);
                case "6" -> highlightMoves(var);
                default -> out.println("not very cash money, your input is invalid brah");
            };
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private String printMenu(String var) {
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

    private void highlightMoves(String var) {
        out.println("Enter the position\n" + "of the piece you would like to highlight: ");
        String position = scanner.next();
        cords = evalPosition(position);
        if (cords.getColumn() == 9){
            out.println("Invlaid input");
            evalInput(var);
        }
        DrawChessBoard.highlightMoves();
        //how do I pass in these parameters?
        DrawChessBoard.drawChessBoard(teamColor, game, cords);
        DrawChessBoard.highlightMoves();
        scanner.next();
        //out.println(printMenu());
        //call highlight moves with cords
        //draw chess board highlighted
        //return printMenu();
        evalInput(var);
    }
    private void makeMove(String var) throws InvalidMoveException {
        //draw chessboard before move
        DrawChessBoard.drawChessBoard(teamColor, game, cords);
        out.println("Enter the position\n" + "of the piece you would like to move: ");
        String position = scanner.next();
        ChessPosition startPos = evalPosition(position);
        if (cords.getColumn() == 9){
            out.println("Invlaid input");
            evalInput(var);
        }
        out.println("Enter the position\n" + "you would like to move the piece to: ");
        String position2 = scanner.next();
        ChessPosition endPos = evalPosition(position2);
        if (cords.getColumn() == 9){
            out.println("Invlaid input");
            evalInput(var);
        }
        //draw chess Board after move
        DrawChessBoard.drawChessBoard(teamColor, game, cords);
        makeMoveHelper(teamColor, game, startPos, endPos);
        evalInput(var);
    }

    private void makeMoveHelper(ChessGame.TeamColor teamColor, ChessGame game, ChessPosition startPos, ChessPosition endPos) throws InvalidMoveException {
        //make move
        ChessMove finalMove;
        Collection<ChessMove> validMoves = game.validMoves(startPos);
        for (ChessMove move : validMoves) {
            ChessPosition validMove = move.getEndPosition();
            if (validMove.equals(endPos)) {
                //move can be made
                if ((board.getPiece(endPos).getPieceType() == ChessPiece.PieceType.PAWN)
                        && (endPos.getRow() == 1 || endPos.getRow() == 8)) {
                    //promoPiece
                    out.println(promotionMenu());
                    String promoInput = scanner.next();
                    ChessPiece.PieceType realPiece = getPromoPiece(promoInput);
                    finalMove = new ChessMove(startPos, endPos, realPiece);
                }
                else{
                    finalMove = new ChessMove(startPos, endPos, null);
                }
                game.makeMove(finalMove);
            }
            else{
                out.println("Invalid Move");
            }
            //implement promotion piece
            //implement checkmate, stalemate...etc
        }
    }

    private String promotionMenu() {
        return """
                Enter the number of the action you want to take
                1. Queen
                2. Rook
                3. Bishop
                4. Knight
                """;
    }

    ChessPiece.PieceType getPromoPiece(String input) {
        return switch (input) {
            case "1" -> ChessPiece.PieceType.QUEEN;
            case "2" -> ChessPiece.PieceType.KNIGHT;
            case "3" -> ChessPiece.PieceType.ROOK;
            case "4" -> ChessPiece.PieceType.BISHOP;
            default -> null;
        };
    }

    private void resign(String var) {
        //make them lose the game
        switch (teamColor.name()) {
            case "BLACK":
                System.out.println("WHITE WINS");
                break;
            case "WHITE":
                System.out.println("BLACK WINS");
                break;
        }
        //DELETE GAME??
        evalInput(var);
    }

    private void leave() {
        //delete game??
    }

    private void redrawChessBoard(String var) {
        //print according to the team they are on
        DrawChessBoard.drawChessBoard(teamColor, game, cords );
        evalInput(var);
    }

    private void GamePlayHelp(String var) {
        out.println("""
                Enter the number of the action you want to take.
                Enter 1 for help
                Enter 2 to draw the Chess Board
                Enter 3 to leave the game
                Enter 4 to move a piece on the chess board. Enter the Coordinates you want to move to ex: b4
                Enter 5 resign from the game
                Enter 6 to highlight the possible moves for a piece
                """);
        evalInput(var);
    }


    private ChessPosition evalPosition(String input) {
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
        if(numOnePos == 9){
            return new ChessPosition(9,9);
        }
        ChessPosition pos = new ChessPosition(numOnePos, numTwoPos);
        //chess position of two integers (x,y) or (row, col)
        return pos;
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
            default -> "9";
        };
    }
};

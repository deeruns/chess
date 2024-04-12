package ui;

import DataAccess.DataAccessException;
import WebSocket.WebSocketFacade;
import chess.*;
//import server.websocket.ConnectionManager;

import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class GamePlayUI {
    private final WebSocketFacade ws;
    Scanner scanner = new Scanner(System.in);
    ChessGame.TeamColor teamColor;
    ChessGame game;
    boolean observing = false;
    int gameID;
    ChessBoard board;
    String authToken;
    ChessPosition cords;


    public GamePlayUI(WebSocketFacade ws, String auth, ChessGame gameChess) {
        this.ws = ws;
        game = gameChess;
        board = game.getBoard();
        board.resetBoard();
        authToken = auth;

    }

    public void setTeamColor(ChessGame.TeamColor color) {
        teamColor = color;
    }

    public void setGameID(int gID) {
        gameID = gID;
    }

    public void setObserve(boolean bool) {
        observing = bool;
    }

    public void joinGamePlay(ChessGame.TeamColor color) throws DataAccessException {
        //DrawChessBoard drawChessBoard = new DrawChessBoard();
        this.teamColor = color;
        if (Objects.equals(WHITE, color)) {
            ws.joinPlayer(authToken,gameID, teamColor);
        }
        else if (Objects.equals(BLACK, color)) {
            ws.joinPlayer(authToken ,gameID, teamColor);
        }
        else {
            ws.joinObserver(authToken,gameID);
        }
    }

    public void evalInput() {
        //out.println(printMenu());
        printMenu();
        out.println(">>>");
        String input = scanner.next();
        try {
            switch (input) {
                case "1" -> gamePlayHelp();
                case "2" -> redrawChessBoard();
                case "3" -> leave();
                case "4" -> makeMove();
                case "5" -> resign();
                case "6" -> highlightMoves();
                default -> out.println("not very cash money, your input is invalid brah");
            };
        } catch (Exception exception) {
            out.println(exception.getMessage());
        }
    }

    private void printMenu() {
        out.println("""
                Enter the number of the action you want to take
                1. Help
                2. Redraw Chess Board
                3. Leave Game
                4. Make a Move
                5. Resign
                6. Highlight Possible Moves
                """);
    }

    private void highlightMoves() {
        if (game.isInCheck(teamColor)){
            ChessPosition king = game.findKingPiece(teamColor);
            DrawChessBoard.drawChessBoard(teamColor, game, king);
            scanner.next();
            System.out.println("Bruh Moment: You are in check and must move the King!");
            evalInput();
        }
        else {
            out.println("Enter the position of the piece you would like to highlight: ");
            String position = scanner.next();
            cords = evalPosition(position);
            if (cords.getColumn() == 9) {
                out.println("Invlaid input");
                evalInput();
            }
            DrawChessBoard.highlightMoves();
            DrawChessBoard.drawChessBoard(teamColor, game, cords);
            DrawChessBoard.highlightMoves();
            evalInput();
        }
    }
    private void makeMove() throws InvalidMoveException, DataAccessException {
        DrawChessBoard.drawChessBoard(teamColor, game, cords);
        out.println("Enter the position of the piece you would like to move: ");
        String position = scanner.next();
        ChessPosition startPos = evalPosition(position);
        out.println("Enter the position you would like to move the piece to: ");
        String position2 = scanner.next();
        ChessPosition endPos = evalPosition(position2);
        //PROMOTION PIECE
        ChessMove move;

        if ((board.getPiece(startPos).getPieceType() == ChessPiece.PieceType.PAWN)
                && (endPos.getRow() == 1 || endPos.getRow() == 8)) {
            out.println(promotionMenu());
            String promoInput = scanner.next();
            ChessPiece.PieceType realPiece = getPromoPiece(promoInput);
            move = new ChessMove(startPos, endPos, realPiece);
        }

        else {
            move = new ChessMove(startPos, endPos, null);
        }
        //CALL WEBSOCKET
        DrawChessBoard.drawChessBoard(teamColor, game, cords);
        ws.makeMove(authToken, gameID, move);
        DrawChessBoard.drawChessBoard(teamColor, game, null);
        System.out.println(SET_TEXT_COLOR_RED + "DONT MOVE UNTIL OPPONENT HAS GONE" + SET_TEXT_COLOR_WHITE);
        evalInput();
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

    private void resign() throws DataAccessException {
        //make them lose the game
        switch (teamColor.name()) {
            case "BLACK":
                System.out.println("WHITE WINS");
                break;
            case "WHITE":
                System.out.println("BLACK WINS");
                break;
        }
        ws.resign(authToken, gameID);
    }

    private void leave() throws DataAccessException {
        ws.leave(authToken, gameID);
    }

    private void redrawChessBoard() {
        //print according to the team they are on
        DrawChessBoard.drawChessBoard(teamColor, game, cords );
        evalInput();
    }

    private void gamePlayHelp() {
        out.println("""
                Enter the number of the action you want to take.
                Enter 1 for help
                Enter 2 to draw the Chess Board
                Enter 3 to leave the game
                Enter 4 to move a piece on the chess board. Enter the Coordinates you want to move to ex: b4
                Enter 5 resign from the game
                Enter 6 to highlight the possible moves for a piece
                """);
        evalInput();
    }


    private ChessPosition evalPosition(String input) {
        int numTwoPos = 0;
        String chars = null;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numTwoPos = Character.getNumericValue(c);
            } else {
                chars = String.valueOf(c);
            }
        }
        assert chars != null;
        int numOnePos = Integer.parseInt(evalNumPos(chars));
        if(numOnePos == 9){
            return new ChessPosition(9,9);
        }
        ChessPosition pos = new ChessPosition(numTwoPos, numOnePos);
        return pos;
    }

    private String evalNumPos(String input){
        return switch (input) {
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

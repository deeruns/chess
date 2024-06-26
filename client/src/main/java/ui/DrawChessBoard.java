package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final int NUM_OF_SQUARES = 8;
    private static final int SQUARE_SIZE = 3;
    private static final int BORDER_WIDTH = 1;
    private static final String EMPTY = "   ";
    private static final String ROOK = " R ";
    private static final String KNIGHT = " N ";
    private static final String BISHOP = " B ";
    private static final String KING = " K ";
    private static final String QUEEN = " Q ";
    private static final String PAWN = " P ";
    private static boolean color;
    private static boolean highlight = false;

    public static void main(String[] args) {
    }

    public static void drawChessBoard(ChessGame.TeamColor teamColor, ChessGame game, ChessPosition position) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        //ChessBoard board = game.getBoard();
        out.print(EscapeSequences.ERASE_SCREEN);
        if (teamColor == ChessGame.TeamColor.BLACK) {
            drawHeadersBlack(out);
            drawBlackBoard(out, game, position);
            out.println();
        } else {
            //draw board from the white perspective for observers and white team color
            drawHeaders(out);
            drawWhiteBoard(out, game, position);
            out.println();
        }
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);

    }

    //draw board
    public static void drawWhiteBoard(PrintStream out, ChessGame game, ChessPosition position) {
        boolean initialColor = color;
        drawSideHeaders(out, 8);
        drawRowsWhite(out, game, position);
        out.println();
        drawHeaders(out);
        color = initialColor;
    }

    public static void drawBlackBoard(PrintStream out, ChessGame game, ChessPosition position) {
        boolean initialColor = color;
        drawSideHeaders(out, 1);
        drawRowsBlack(out, game, position);
        out.println();
        drawHeadersBlack(out);
        color = initialColor;
    }

    public static void drawRowsOrganizer(PrintStream out, ChessGame game, ChessPosition position, ChessBoard board, int i, int j){
        changeColorWhiteTeam(out);
        ChessPosition currPos = new ChessPosition(i, j);
        highlightPrinter(out, game, position, currPos);
        if (board.getPiece(currPos) != null) {
            if(position != null) {
                if (currPos.equals(position)) {
                    out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    printPiece(out, board.getPiece(currPos).getPieceType(), board.getPiece(currPos).getTeamColor());
                }
                else {
                    printPiece(out, board.getPiece(currPos).getPieceType(), board.getPiece(currPos).getTeamColor());
                }
            }
            else {
                printPiece(out, board.getPiece(currPos).getPieceType(), board.getPiece(currPos).getTeamColor());
            }

        }
        else {
            out.print(EMPTY);
        }
    }
    public static void drawRowsBlack(PrintStream out, ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        for (int i = 1; i < 9; i++) {
            for (int j = 8; j > 0; j--) {
                drawRowsOrganizer(out, game, position, board,i,j);
            }
            drawSideHeaders(out, i);
            if (i < 8) {
                out.println();
                drawSideHeaders(out, i + 1);
            }
            color = !color;
        }
        // drawSideHeaders(out, row+1);
        setBackgroundDarkGrey(out);
        //out.println();
    }

    public static void drawRowsWhite(PrintStream out, ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                drawRowsOrganizer(out, game, position, board,i,j);
            }
            drawSideHeaders(out, i);
            if (i > 1) {
                out.println();
                drawSideHeaders(out, i - 1);
            }
            color = !color;
        }
        // drawSideHeaders(out, row+1);
        setBackgroundDarkGrey(out);
        //out.println();
    }

    public static void highlightPrinter(PrintStream out, ChessGame game, ChessPosition position, ChessPosition currPos) {
        //Collection<ChessMove> validMoves = game.validMoves(position);
        if (highlight) {
            Collection<ChessMove> validMoves = game.validMoves(position);
            if(validMoves != null) {
                for (ChessMove move : validMoves) {
                    ChessPosition endPos = move.getEndPosition();
                    if (currPos.equals(endPos)) {
                        if (!color) {
                            setBackgroundDarkGreen(out);
                            break;
                        } else {
                            setBackgroundGreen(out);
                            break;
                        }
                    }
                }
            }
        }
    }


    public static void drawHeaders(PrintStream out){
        setBackgroundGrey(out);
        String[] topHeaders = {"    a ", " b ", " c ", " d ", " e ", " f ", " g ", " h    "};
        //print left header
        for (int col = 0; col < NUM_OF_SQUARES; col++){
            singleHeader(out, topHeaders[col]);
        }
        setBackgroundDarkGrey(out);
        out.println();
    }
    public static void drawHeadersBlack(PrintStream out){
        setBackgroundGrey(out);
        String[] topHeaders = {"    h ", " g ", " f ", " e ", " d ", " c ", " b ", " a    "};

        //print left header
        for (int col = 0; col < NUM_OF_SQUARES; col++){
            singleHeader(out, topHeaders[col]);
        }
        setBackgroundDarkGrey(out);
        out.println();
    }

    public static void singleHeader(PrintStream out, String colNum){
        printHeaderText(out, colNum);
    }
    public static void printHeaderText(PrintStream out, String colNum){
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(colNum);
        setBackgroundGrey(out);
    }
    public static void drawSideHeaders(PrintStream out, int row){
        setBackgroundGrey(out);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(" " + (row) + " ");
        setBackgroundDarkGrey(out);
    }

    public static void highlightMoves(){
        if (highlight) {
            highlight = false;
        } else {
            highlight = true;
        }
    }

    //set colors
    private static void setBackgroundWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }
    private static void setBackgroundBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
    private static void setBackgroundGrey(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
    }
    private static void setBackgroundDarkGrey(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
    }
    private static void setBackgroundGreen(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_GREEN);
    }
    private static void setBackgroundDarkGreen(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
    }

    //print pieces
    private static void printPiece(PrintStream out, ChessPiece.PieceType piece, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE){
            setWhitePiece(out);
        }
        else if (color == ChessGame.TeamColor.BLACK){
            setBlackPiece(out);
        }

        switch(piece){
            case KING -> out.print(" K ");
            case QUEEN -> out.print(" Q ");
            case BISHOP -> out.print(" B ");
            case KNIGHT -> out.print(" N ");
            case ROOK -> out.print(" R ");
            case PAWN -> out.print(" P ");

        }
        //out.print(player);
        //setBackgroundWhite(out);
    }
    private static void setWhitePiece(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
    private static void setBlackPiece(PrintStream out){
        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
    }
    private static void changeColorWhiteTeam(PrintStream out){
        setBackgroundWhite(out);
        if (color == true) {
            //print white square
            setBackgroundBlack(out);
            color = false;
        } else {
            color = true;
        }
    }

}


package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
    private static  boolean color;

    public static void main(String[] args) {
        drawChessBoard();
    }
    public static void drawChessBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessPiece[][] board = new ChessGame().getBoard().getBoard();
        ChessPiece[][] blackBoard = upsideDownBoard(board);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out);
        drawWhiteBoard(out, board);
        out.println();
        drawHeadersBlack(out);
        drawBlackBoard(out, blackBoard);
        out.println("Successfully Joined Game");
        //setBackgroundBlack(out);
    }
    //draw board
    public static void drawWhiteBoard(PrintStream out, ChessPiece[][] board){
        boolean initialColor = color;
        drawSideHeaders(out, 8);
        drawRowsWhite(out, board);
        out.println();
        drawHeaders(out);
        color = initialColor;
    }
    public static void drawBlackBoard(PrintStream out, ChessPiece[][] board){
        boolean initialColor = color;
        drawSideHeaders(out, 1);
        drawRowsBlack(out, board);
        out.println();
        drawHeadersBlack(out);
        color = initialColor;
    }

public static void  drawRowsBlack(PrintStream out, ChessPiece[][] board) {
    //drawHeaders(out);
    for(int i = 0; i < board.length; i++){
        for (int j = 0; j < board.length; j++){
            changeColorWhiteTeam(out);
            if (board[i][j] != null){
                printPiece(out, board[i][j].getPieceType(), board[i][j].getTeamColor());
            }
            else{
                out.print(EMPTY);
            }
        }
        drawSideHeaders(out, i+1);
        if (i < 7){
            out.println();
            drawSideHeaders(out, i+2);
        }
        color = !color;
    }
    // drawSideHeaders(out, row+1);
    setBackgroundDarkGrey(out);
    //out.println();
}
    public static void  drawRowsWhite(PrintStream out, ChessPiece[][] board) {
        //drawHeaders(out);
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                changeColorWhiteTeam(out);
                if (board[i][j] != null){
                    printPiece(out, board[i][j].getPieceType(), board[i][j].getTeamColor());
                }
                else{
                    out.print(EMPTY);
                }
            }
            drawSideHeaders(out, 8 - i);
            if (i < 7){
                out.println();
                drawSideHeaders(out, 7 - i);
            }
            color = !color;
        }
        // drawSideHeaders(out, row+1);
        setBackgroundDarkGrey(out);
        //out.println();
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

    //set colors
    private static void setBackgroundWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }
    private static void setBackgroundBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
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

    private static void changeColorBlackTeam(PrintStream out){
        setBackgroundBlack(out);
        if (color == true) {
            //print white square
            setBackgroundWhite(out);
            color = false;
        } else {
            color = true;
        }
    }
    public static ChessPiece[][] upsideDownBoard(ChessPiece[][] array) {
        int rows = array.length;
        int cols = array[0].length;
        ChessPiece[][] upsideDown = new ChessPiece[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                upsideDown[i][j] = array[rows - 1 - i][cols - 1 - j];
            }
        }

        return upsideDown;
    }


}


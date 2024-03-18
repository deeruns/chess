package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class DrawChessBoard {
    private static final int NUM_OF_SQUARES = 8;
    private static final int SQUARE_SIZE = 3;
    private static final int BORDER_WIDTH = 1;
    private static final String EMPTY = " ";
    private static final String ROOK = " R ";
    private static final String KNIGHT = " N ";
    private static final String BISHOP = " B ";
    private static final String KING = " K ";
    private static final String QUEEN = " Q ";
    private static final String PAWN = " P ";
    private static  boolean color;

    public static void main(String[] args) {
        DrawChessBoard();
    }
    public static void DrawChessBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        drawHeaders(out);
        drawBoard(out);
    }
    //draw board
    public static void drawBoard(PrintStream out){
        boolean initialColor = color;
        for (int row = 0; row < NUM_OF_SQUARES; row++){
//            for(int thick = 0; thick < 2; thick++) {
//                drawRows(out);
//            }
            drawSideHeaders(out, row+1);
            drawRows(out);
            drawSideHeaders(out, row+1);
            out.println();
            color = !color;
        }
        drawHeaders(out);
        color = initialColor;
    }

    public static void  drawRows(PrintStream out) {
        //drawHeaders(out);

            for (int col = 0; col < NUM_OF_SQUARES; col++) {
                setBackgroundBlack(out);
                    if (color == true) {
                        //print white square
                        setBackgroundWhite(out);
                        color = false;
                    } else {
                        color = true;
                    }
                    for (int i = 0; i < SQUARE_SIZE; i++) {
                        out.print(EMPTY);
                    }
            }
       // drawSideHeaders(out, row+1);
        setBackgroundDarkGrey(out);
            //out.println();
    }

    public static void drawHeaders(PrintStream out){
        setBackgroundGrey(out);
        String[] topHeaders = {"    a ", " b ", " c ", " d ", " e ", " f ", " g ", " i    "};
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
    //draw header
    //draw left header
    //draw right header
    //draw bottom header

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
    private static void printPiece(PrintStream out, String player, String col, String row) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        //rows can determine color
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);

        out.print(player);

        setBackgroundWhite(out);
    }

}


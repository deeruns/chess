import chess.*;
import ui.ConsoleUI;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        ConsoleUI console = new ConsoleUI();
        System.out.println("Welcome to Davin Thompson's Chess Server");
        Scanner scanner = new Scanner(System.in);
        String message = "";

        while (!message.contains(";)")){

            try{
                System.out.println(console.clientHelp());
                String input = scanner.nextLine();
                message = console.evalInput(input);
                System.out.println(message);
            }
            catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        System.out.println("Good night, have a good sleep, dream big, dream safe, and remember you are loved by god, alright, see you tomorrow every body");
    }
    }

import DataAccess.DataAccessException;
import ui.ConsoleUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws DataAccessException {
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

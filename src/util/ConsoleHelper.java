package util;

public class ConsoleHelper {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static void printlnRed(String message) {
        println(message, ANSI_RED);
    }

    public static void printlnYellow(String message) {
        println(message, ANSI_YELLOW);
    }

    public static void printlnPurple(String message) {
        println(message, ANSI_PURPLE);
    }

    public static void printlnBlue(String message) {
        println(message, ANSI_BLUE);
    }
    public static void printlnDefault(String message) {
        System.out.println(message);
    }

    private static void println(String message, String color) {
        System.out.println(color + message + ANSI_RESET);
    }
}

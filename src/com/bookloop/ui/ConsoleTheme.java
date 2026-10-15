package com.bookloop.ui;

/**
 * ANSI styling and terminal formatting utilities for BookLoop AI.
 */
public class ConsoleTheme {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_GREEN = "\u001B[42m";

    public static String cyan(String s) { return CYAN + s + RESET; }
    public static String green(String s) { return GREEN + s + RESET; }
    public static String yellow(String s) { return YELLOW + s + RESET; }
    public static String bold(String s) { return BOLD + s + RESET; }
    public static String red(String s) { return RED + s + RESET; }
    public static String purple(String s) { return PURPLE + s + RESET; }
    public static String blue(String s) { return BLUE + s + RESET; }

    public static void printDivider() {
        System.out.println(CYAN + "━".repeat(78) + RESET);
    }

    public static void printThinDivider() {
        System.out.println(DIM + "─".repeat(78) + RESET);
    }
}

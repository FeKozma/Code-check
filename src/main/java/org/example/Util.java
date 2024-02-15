package org.example;

public interface Util {
    // Program variables
    static final boolean LOG = false;
    static final String PATH = "C:/Users/Felix/Repo/util/";

    // Styling
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    static void log(String msg) {
        if (LOG) System.out.println(msg);
    }
    static void log(String msg, boolean cond) {
        if (cond) System.out.println(msg);
    }
}

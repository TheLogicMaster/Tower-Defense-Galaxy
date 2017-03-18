package com.logicmaster63.tdworld.tools;

public class Tools {

    public static int[] doubleParseInt(String string) {
        char c = 'a';
        int increment = 0;
        while(c != ' ') {
            c = string.charAt(increment);
            increment++;
        }
        return new int[]{Integer.parseInt(string.substring(0, increment - 1)), Integer.parseInt(string.substring(increment, string.length()))};
    }

    public static int[] trippleParseInt(String string) {
        char c = '?';
        int increment0 = 0;
        while (c != ' ') {
            c = string.charAt(increment0);
            increment0++;
        }
        c = '?';
        int increment1 = increment0 + 1;
        while (c != ' ') {
            c = string.charAt(increment1);
            increment1++;
        }
        return new int[]{Integer.parseInt(string.substring(0, increment0 - 1)), Integer.parseInt(string.substring(increment0, increment1 - 1)), Integer.parseInt(string.substring(increment1, string.length()))};
    }
}

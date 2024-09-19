package com.cjp.olc.utility;

import java.util.Scanner;

/**
 * @author CJP
 * @version 1.0
 */
public class Utilities {
    public static int getInput_Int() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static String getInput_Str() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

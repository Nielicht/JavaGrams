package IO;

import java.util.Scanner;

public class KeyboardInput {
    private static final Scanner sc = new Scanner(System.in);

    private KeyboardInput() {
        throw new IllegalStateException("Utility class");
    }

    public static String getString() {
        return sc.nextLine();
    }

    public static char getChar() {
        return sc.nextLine().charAt(0);
    }

    public static int getInt() {
        return Integer.parseInt(sc.nextLine());
    }
}

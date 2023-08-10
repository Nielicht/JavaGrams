package IO;

import java.util.Map;

public class Printer {

    public Printer() {
        throw new IllegalStateException("Utility class");
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static void print(String[] msgs) {
        for (String msg : msgs) {
            System.out.println(msg);
        }
    }

    public static void print(Map<String, String> msgs) {
        for (String msg : msgs.keySet()) {
            System.out.println(msg + " → " + msgs.get(msg));
        }
    }
}

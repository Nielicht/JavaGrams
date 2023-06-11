package logic;

import java.io.*;

public class Conversor {\

    private Conversor() {
        throw new IllegalStateException("Utility class");
    }

    // 0 3 3 2 5
    //
    // 0 0 1 0 1    2
    // 0 1 0 0 1    2
    // 0 1 1 0 1    3
    // 0 0 0 1 1    2
    // 0 1 1 1 1    4

    public static int[][] generateBoard(String nonogramPath) {
        int[][] board = new int[0][0];

        try (BufferedReader br = new BufferedReader(new FileReader(nonogramPath))) {
            String line;
            int columns = 0;
            int currentColumns = 0;
            int rows = 0;
            int number;

            while ((line = br.readLine()) != null) {
                rows++;
                if (columns == 0) columns = line.length();
                else if ((currentColumns = line.length()) != columns)
                    throw new NumberFormatException("What in tarnation");

                for (char numberChar : line.toCharArray()) {
                    number = Character.getNumericValue(numberChar);

                    switch (number) {
                        default -> throw new NumberFormatException("File contains non-expected number");
                        case -1 -> throw new NumberFormatException("File contains non-number characters");
                        case 0 ->
                        case 1 ->
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return board;
    }

    public static int[] generateLegend(int[][] board) {
        int[] legend = new int[board.length + board[0].length];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1) {
                    legend[x]++;
                    legend[board[x].length + y]++;
                }
            }
        }

        return legend;
    }
}
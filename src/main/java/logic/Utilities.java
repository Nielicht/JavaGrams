package logic;

import java.io.*;

public class Utilities {

    private Utilities() {
        throw new IllegalStateException("Utility class");
    }

    public static int[][] generateBoard(String nonogramPath) {
        int[][] board = new int[0][0];
        nonogramPath = ClassLoader.getSystemResource(nonogramPath).getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(nonogramPath))) {
            String[] lines = br.lines().toArray(String[]::new);

            int rows = lines.length;
            int columns = lines[0].length();
            board = new int[rows][columns];

            int x = -1;
            int y = -1;
            int number;

            for (String line : lines) {
                y++;

                // Checks that the number of columns is coherent
                if (line.length() != columns) throw new NumberFormatException("Columns in file are not coherent");

                // Processes lines in a nonogram file
                for (char numberChar : line.toCharArray()) {
                    x++;
                    number = Character.getNumericValue(numberChar);

                    switch (number) {
                        case 1 -> board[y][x] = 1;
                        case 0 -> board[y][x] = 0;
                        default -> throw new NumberFormatException("File contains non-expected value");
                    }
                }
                x = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
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
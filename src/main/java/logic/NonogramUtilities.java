package logic;

import IO.FileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class NonogramUtilities {

    private NonogramUtilities() {
        throw new IllegalStateException("Utility class");
    }

    public static int[][] generateBoard(String nonogramPath) {
        int[][] board = new int[0][0];

        try (BufferedReader br = FileSystem.getResourceBReader(nonogramPath)) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return board;
    }

    public static int[][] generateLegend(int[][] board) {
        // Setup
        ArrayList<ArrayList<Integer>> listWithLegends = new ArrayList<>();
        int nColumns = board[0].length;
        int nRows = board.length;
        int sum = 0;

        // Arraylist setup
        for (int i = 0; i < nRows + nColumns; i++) {
            listWithLegends.add(new ArrayList<>());
        }

        // Rows legend calculation
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nColumns; x++) {
                switch (board[y][x]) {
                    case 1 -> {
                        sum++;
                        if (x == nColumns - 1 && sum != 0) {
                            listWithLegends.get(y).add(sum);
                            sum = 0;
                        }
                    }
                    case 0 -> {
                        if (sum != 0) {
                            listWithLegends.get(y).add(sum);
                            sum = 0;
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + board[y][x]);
                }
            }
        }

        // Columns legend calculation
        for (int x = 0; x < nColumns; x++) {
            for (int y = 0; y < nRows; y++) {
                switch (board[y][x]) {
                    case 1 -> {
                        sum++;
                        if (y == nRows - 1 && sum != 0) {
                            listWithLegends.get(nRows + x).add(0, sum);
                            sum = 0;
                        }
                    }
                    case 0 -> {
                        if (sum != 0) {
                            listWithLegends.get(nRows + x).add(0, sum);
                            sum = 0;
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + board[y][x]);
                }
            }
        }

        // Conversion from ArrayList of Arraylists of integer wrappers (Integer) to an array of arrays of primitive ints
        int[][] finalLegend = new int[listWithLegends.size()][];
        int[] finalRow;
        for (int row = 0; row < listWithLegends.size(); row++) {
            ArrayList<Integer> rowInList = listWithLegends.get(row);
            finalRow = new int[rowInList.size()];

            for (int rowIndex = 0; rowIndex < rowInList.size(); rowIndex++) {
                finalRow[rowIndex] = rowInList.get(rowIndex);
            }

            finalLegend[row] = finalRow;
        }

        // Return of calculated legend
        return finalLegend;
    }
}
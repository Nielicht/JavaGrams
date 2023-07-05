package logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class CLIPlayer {
    public static void main(String[] args) throws IOException {
        String path = ClassLoader.getSystemResource("nFiles/test.txt").getPath();
        Nonogram nonogram = new Nonogram(path); // Note: it breaks with non-square matrices :(

        while (!nonogram.isSolved()) {
            drawBoard(nonogram);
            getInput(nonogram);
        }

        drawBoard(nonogram);
        System.out.println("Nonogram solved!");
    }

    private static void getInput(Nonogram ng) throws IOException {
        System.out.println("Press f (fill), b (blank) or x (crossed), then input coordinates separated by commas");
        System.out.println("For example: f2,3\n");

        Scanner sc = new Scanner(System.in);
        StringBuilder input = new StringBuilder(sc.nextLine());
        char action = Character.toLowerCase(input.charAt(0));
        input.deleteCharAt(0);

        String[] coordinates = input.toString().split(",");
        int x = Integer.parseInt(coordinates[0]) - 1;
        int y = Integer.parseInt(coordinates[1]) - 1;
        switch (action) {
            case 'f' -> ng.transaction(x, y, CellState.FILLED);
            case 'b' -> ng.transaction(x, y, CellState.HOLLOW);
            case 'x' -> ng.transaction(x, y, CellState.CROSSED);
            default -> throw new UnsupportedEncodingException("What");
        }
    }

    private static void drawBoard(Nonogram ng) {
        StringBuilder sb = new StringBuilder("\n");
        CellState[][] currentBoard = ng.getCellState();

        int[][] columnsLegend = ng.getColumnsLegend();
        int biggest = 0;
        for (int[] column : columnsLegend) if (column.length > biggest) biggest = column.length;
        for (int currentHeight = biggest; currentHeight > 0; currentHeight--) {
            for (int[] column : columnsLegend) {
                if (column.length >= currentHeight) {
                    sb.append(column[currentHeight - 1] + " ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }

        int[][] rowsLegend = ng.getRowsLegend();
        for (int y = 0; y < currentBoard.length; y++) {
            for (int x = 0; x < currentBoard[y].length; x++) {
                switch (currentBoard[y][x]) {
                    case HOLLOW -> sb.append("□ ");
                    case FILLED -> sb.append("▣ ");
                    case CROSSED -> sb.append("⊠ ");
                }
            }
            for (int value : rowsLegend[y]) {
                sb.append(value + " ");
            }
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }
}
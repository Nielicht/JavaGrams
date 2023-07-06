package logic;

import java.io.IOException;
import java.util.Scanner;

public class CLIPlayer {
    public static void main(String[] args) throws IOException {
        // Nonogram generation
        String path = ClassLoader.getSystemResource("nFiles/test.txt").getPath();
        Nonogram nonogram = new Nonogram(path);

        // CLI Player
        displayWelcomeMessage();
        while (!nonogram.isSolved()) {
            drawBoard(nonogram);
            getInput(nonogram);
        }

        // Completed screen
        drawBoard(nonogram);
        System.out.println("Nonogram solved!");
    }

    private static void getInput(Nonogram ng) throws IOException {
        // Input retrieval
        char[] inputs = new char[3]; // [0] for coordinate "x", [1] for coordinate "y" and [2] reserved for special operation (if any)
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 2; i++) {
            inputs[i] = sc.nextLine().charAt(0);

            // If a letter is retrieved, it is stored in the special operation index [2]
            if (Character.isLetter(inputs[i])) {
                inputs[2] = inputs[i];
                i = -1; // Reset the loop
            }
        }

        // Data adaptation
        int x = Character.getNumericValue(inputs[0]) - 1;
        int y = Character.getNumericValue(inputs[1]) - 1;
        char operation = inputs[2];

        // Operation execution
        switch (operation) {
            case 'b' -> ng.transaction(x, y, CellState.HOLLOW);
            case 'x' -> ng.transaction(x, y, CellState.CROSSED);
            default -> ng.transaction(x, y, CellState.FILLED);
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("\nEnter the coordinates, one for each (Enter) press.");
        System.out.println("You can enter a special letter in any given moment to change the final outcome: (b) for blank, (x) to cross, (any) for standard fill.");
        System.out.println("Note that entering any letter will drop any already given coordinate.");
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

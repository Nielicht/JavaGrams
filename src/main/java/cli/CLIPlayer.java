package cli;

import IO.FileSystem;
import IO.KeyboardInput;
import logic.Tiles;
import logic.Nonogram;

import java.io.IOException;
import java.nio.file.Path;

public class CLIPlayer {
    public static void main(String[] args) throws IOException {
        // Nonogram generation
        Nonogram ng = getNonogramFromSelection();

        // Initiate game
        displayWelcomeMessage();
        playGame(ng);

        // Solved screen
        drawBoard(ng);
        System.out.println("Nonogram solved!");
    }

    private static Nonogram getNonogramFromSelection() throws IOException {
        Path[] paths = FileSystem.getFPathsFromResourceFolder("nFiles/");
        String[] choices = new String[paths.length];

        if (paths.length == 0) {
            System.out.println("There are no files!");
            System.exit(1);
        }

        for (int i = 0; i < choices.length; i++) {
            choices[i] = (i + 1) + ". " + paths[i].getFileName().toString();
        }

        System.out.println("Select the file with the desired nonogram:\n");
        for (String choice : choices) {
            System.out.println(choice);
        }

        System.out.print("\nChoice: ");

        String selection = paths[KeyboardInput.getInt() - 1].toString();
        Nonogram ng = new Nonogram(selection);
        return ng;
    }

    private static void playGame(Nonogram ng) {
        while (!ng.isSolved()) {
            drawBoard(ng);
            getInputAndExecute(ng);
        }
    }

    private static void getInputAndExecute(Nonogram ng) {
        // KeyboardInput retrieval
        char[] inputs = new char[3]; // [0] for coordinate "x", [1] for coordinate "y" and [2] reserved for special operation (if any)
        for (int i = 0; i < 2; i++) {
            inputs[i] = KeyboardInput.getChar();

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
        executeOperation(ng, x, y, operation);
    }

    public static void executeOperation(Nonogram ng, int x, int y, char operation) {
        switch (operation) {
            case 'b' -> ng.transaction(x, y, Tiles.HOLLOW);
            case 'x' -> ng.transaction(x, y, Tiles.CROSSED);
            default -> ng.transaction(x, y, Tiles.FILLED);
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("\nEnter the coordinates, one for each (Enter) press.");
        System.out.println("You can enter a special letter in any given moment to change the final outcome: (b) for blank, (x) to cross, (any) for standard fill.");
        System.out.println("Note that entering any letter will drop any already given coordinate.");
        System.out.println("\nPress any key to continue...");
        KeyboardInput.getString();
    }

    private static void drawBoard(Nonogram ng) {
        StringBuilder sb = new StringBuilder("\n");
        Tiles[][] tiles = ng.getTileState();

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
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                switch (tiles[y][x]) {
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

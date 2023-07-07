package logic;

import java.util.Arrays;

public class Nonogram {
    private int[][] board;
    private int[][] legend;
    private CurrentBoard[][] currentBoard;

    public Nonogram(String path) {
        this.board = NonogramUtilities.generateBoard(path);
        this.legend = NonogramUtilities.generateLegend(this.board);
        this.currentBoard = new CurrentBoard[this.board.length][this.board[0].length];
        for (CurrentBoard[] currentBoards : this.currentBoard) {
            Arrays.fill(currentBoards, CurrentBoard.HOLLOW);
        }
    }

    public void transaction(int x, int y, CurrentBoard state) {
        try {
            this.currentBoard[y][x] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nThe coordinate does not exist");
        }
    }

    public boolean isSolved() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1 && (this.currentBoard[y][x] == CurrentBoard.CROSSED || this.currentBoard[y][x] == CurrentBoard.HOLLOW))
                    return false;
            }
        }

        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getLegend() {
        return legend;
    }

    public int getColumns() {
        return this.board[0].length;
    }

    public int getRows() {
        return this.board.length;
    }

    public int[][] getRowsLegend() {
        int nRows = this.getRows();
        return Arrays.copyOfRange(this.legend, 0, nRows);
    }

    public int[][] getColumnsLegend() {
        int nRows = this.getRows();
        return Arrays.copyOfRange(this.legend, nRows, this.legend.length);
    }

    public CurrentBoard[][] getCellState() {
        return currentBoard;
    }

    public CurrentBoard getCellState(int x, int y) {
        return this.currentBoard[y][x];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int[] row : board) {
            sb.append("\n");
            for (int valueAtRow : row) {
                sb.append(valueAtRow);
            }
        }

        sb.append("\n\nRaw legend variable: ");

        for (int[] row : legend) {
            sb.append("\n");
            for (int valueAtRow : row) {
                sb.append(valueAtRow + " ");
            }
        }

        return sb.toString();
    }
}

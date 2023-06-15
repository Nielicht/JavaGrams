package logic;

import java.util.Arrays;

enum CellState {
    HOLLOW, FILLED, CROSSED
}

public class Nonogram {
    private int[][] board;
    private int[] legend;
    private CellState[][] cellState;

    public Nonogram(String path) {
        this.board = Utilities.generateBoard(path);
        this.legend = Utilities.generateLegend(this.board);
        this.cellState = new CellState[this.board.length][this.board[0].length];
        for (CellState[] cellStates : this.cellState) {
            Arrays.fill(cellStates, CellState.HOLLOW);
        }
    }

    public void transaction(int x, int y, CellState state) {
        this.cellState[y][x] = state;
    }

    public boolean isSolved() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1 && (this.cellState[y][x] == CellState.CROSSED || this.cellState[y][x] == CellState.HOLLOW))
                    return false;
            }
        }

        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[] getLegend() {
        return legend;
    }

    public int getColumns() {
        return this.board[0].length;
    }

    public int getRows() {
        return this.board.length;
    }

    public int[] getRowsLegend() {
        int nRows = this.getRows();
        return Arrays.copyOfRange(this.legend, nRows, this.legend.length);
    }

    public int[] getColumnsLegend() {
        int nColumns = this.getColumns();
        return Arrays.copyOfRange(this.legend, 0, nColumns);
    }

    public CellState[][] getCellState() {
        return cellState;
    }

    public CellState getCellState(int x, int y) {
        return this.cellState[y][x];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int[] row : board) {
            sb.append("\n");
            for (int column : row) {
                sb.append(column);
            }
        }

        sb.append("\n\nRaw legend variable: ");

        for (int legendNumber : legend) {
            sb.append(legendNumber);
        }

        int[] columnsLegend = this.getColumnsLegend();
        sb.append("\nLegend for columns: ");
        for (int column : columnsLegend) {
            sb.append(column);
        }

        int[] rowsLegend = this.getRowsLegend();
        sb.append("\nLegend for rows: ");
        for (int row : rowsLegend) {
            sb.append(row);
        }

        return sb.toString();
    }
}

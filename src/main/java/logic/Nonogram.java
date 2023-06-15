package logic;

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

        for (CellState[] cellStateRow : this.cellState) {
            for (CellState cellStateColumn : cellStateRow) {
                cellStateColumn = CellState.HOLLOW;
            }
        }
    }

    public void transaction(int x, int y, int type) {
        switch (type) {
            case 0 -> cellState[y][x] = CellState.FILLED;
            case 1 -> cellState[y][x] = CellState.HOLLOW;
            case 2 -> cellState[y][x] = CellState.CROSSED;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public boolean checkSolved() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (x == 1 && (cellState[y][x] == CellState.CROSSED || cellState[y][x] == CellState.HOLLOW))
                    return false;
            }
        }

        return true;
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

        sb.append("\n\n");

        for (int legendNumber : legend) {
            sb.append(legendNumber);
        }

        return sb.toString();
    }
}

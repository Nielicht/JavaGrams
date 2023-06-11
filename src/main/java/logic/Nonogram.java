package logic;

enum TileState {
    HOLLOW, FILLED, CROSSED
}

public class Nonogram {
    private int[][] board;
    private int[] legend;
    private TileState[][] tileState;

    public Nonogram(String path) {

    }

    public void loadBoard(int[][] board) {
        if (board.length != this.board.length || board[0].length != this.board[0].length)
            throw new IllegalArgumentException("Size not equals!");

        this.legend = Conversor.generateLegend(board);
        this.board = board;
    }
}

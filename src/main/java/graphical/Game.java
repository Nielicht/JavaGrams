package graphical;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Nonogram;
import logic.Tiles;

public class Game extends Scene {
    private Button[][] tiles;
    private Nonogram nonogram;

    public Game(String nonogramPath) {
        super(new GridPane());
        this.getStylesheets().add("css/player.css");
        this.nonogram = new Nonogram(nonogramPath);
        this.tiles = new Button[this.nonogram.getRows()][this.nonogram.getColumns()];
        setupGrid();
    }

    private void setupGrid() {
        GridPane gp = (GridPane) this.getRoot();
        gp.getStyleClass().add("gridpane");
        fillGrid(gp);
        setupButtons();
//        System.out.println("Number of rows " + gp.getRowCount() + " ; Number of columns: " + gp.getColumnCount());
    }

    private void fillGrid(GridPane gp) {
        int width = this.nonogram.getColumns();
        int height = this.nonogram.getRows();
        int[][] rowsLegend = this.nonogram.getRowsLegend();
        int[][] columnsLegend = this.nonogram.getColumnsLegend();
        addColumnLegends(gp, columnsLegend);
        addButtons(gp, width, height, getBiggest(columnsLegend));
        addRowLegends(gp, rowsLegend, width, getBiggest(columnsLegend));
        addConstraints(gp, width + getBiggest(rowsLegend), height + getBiggest(columnsLegend));
    }

    private void addColumnLegends(GridPane gp, int[][] columnsLegend) {
        for (int x = 0; x < columnsLegend.length; x++) {
            for (int y = columnsLegend[x].length - 1; y >= 0; y--) {
                Text text = new Text(String.valueOf(columnsLegend[x][(columnsLegend[x].length - 1) - y]));
                text.getStyleClass().add("text");
                gp.add(text, x, y);
            }
        }
    }

    private void addRowLegends(GridPane gp, int[][] rowsLegend, int wGap, int hGap) {
        for (int y = 0; y < rowsLegend.length; y++) {
            for (int x = 0; x < rowsLegend[y].length; x++) {
                Text text = new Text(String.valueOf(rowsLegend[y][x]));
                text.getStyleClass().add("text");
                gp.add(text, x + wGap, y + hGap);
            }
        }
    }

    private int getBiggest(int[][] legend) {
        int biggest = 0;
        for (int[] element : legend) {
            if (biggest < element.length) biggest = element.length;
        }
        return biggest;
    }

    private void addButtons(GridPane gp, int width, int height, int hGap) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.println("adding button at [" + x + ", " + y + "]");
                this.tiles[y][x] = new Button();
                this.tiles[y][x].setId(x + ":" + y);
                gp.add(this.tiles[y][x], x, y + hGap);
            }
        }
    }

    private void addConstraints(GridPane gp, int width, int height) {
        RowConstraints rowConstraint = new RowConstraints(32);
        ColumnConstraints columnConstraint = new ColumnConstraints(32);
        for (int x = 0; x < width; x++) gp.getColumnConstraints().add(columnConstraint);
        for (int y = 0; y < height; y++) gp.getRowConstraints().add(rowConstraint);
    }

    private Tiles styleToTile(String finalStyle) {
        Tiles tile = null;
        switch (finalStyle) {
            case "buttonBlank" -> tile = Tiles.HOLLOW;
            case "buttonFilled" -> tile = Tiles.FILLED;
            case "buttonCrossed" -> tile = Tiles.CROSSED;
            default -> throw new IllegalStateException("Unexpected value: " + finalStyle);
        }
        return tile;
    }

    private void setupButtons() {
        for (Button[] tileRow : this.tiles) {
            for (Button tile : tileRow) {
                tile.getStyleClass().clear();
                tile.getStyleClass().add("buttonBlank");
                tile.setOnMouseClicked(mouseEvent -> {
                    if (tile.getStyleClass().size() > 1) throw new RuntimeException("Too many style classes");
                    String style = tile.getStyleClass().get(0);
                    String finalStyle = "buttonBlank";
                    MouseButton mouseButton = mouseEvent.getButton();

                    switch (mouseButton) {
                        case PRIMARY -> {
                            if (style.equals("buttonBlank") || style.equals("buttonCrossed"))
                                finalStyle = "buttonFilled";
                        }
                        case SECONDARY -> {
                            if (style.equals("buttonBlank") || style.equals("buttonFilled"))
                                finalStyle = "buttonCrossed";
                        }
                        default -> {
                            System.out.println("Not this button DUMBASS");
                            return;
                        }
                    }

                    tile.getStyleClass().clear();
                    tile.getStyleClass().add(finalStyle);
                    gameUpdate(tile.getId(), finalStyle);
                });
            }
        }
    }

    private void gameUpdate(String id, String finalStyle) {
        int x = Integer.parseInt(id.split(":")[0]);
        int y = Integer.parseInt(id.split(":")[1]);

        this.nonogram.transaction(x, y, styleToTile(finalStyle));

        if (this.nonogram.isSolved()) triggerEnding();
    }

    private void triggerEnding() {
        BorderPane bp = new BorderPane();
        bp.getStylesheets().add("css/player.css");
        bp.getStyleClass().add("win");
        GUIPlayer.loadScene(new Scene(bp), 608, 718, "audio/toad.wav");
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(7.5), (actionEvent) -> {
            GUIPlayer.loadScene(new MainMenu(), 400, 400, "audio/menu.wav");
        }));
        tl.playFromStart();
    }
}

package graphical;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.util.Duration;
import logic.Nonogram;
import logic.Tiles;

public class Game extends Scene {
    private Button[][] tiles;
    private Nonogram nonogram;
    private String skinPath;
    private String winAudio;

    public Game(String nonogramPath, String winAudio, String skinPath) {
        super(new Pane());
        this.nonogram = new Nonogram(nonogramPath);
        this.tiles = new Button[this.nonogram.getRows()][this.nonogram.getColumns()];
        this.winAudio = winAudio;
        this.skinPath = skinPath;
        setupScene();
    }

    private void setupScene() {
        this.getStylesheets().add(this.skinPath);
        Pane pane = (Pane) this.getRoot();
        pane.getStyleClass().add("innerPane");
        GridPane nonogram = getNonogram();
        GridPane columnsLegend = getColumnLegend();
        GridPane rowsLegend = getRowLegend();
        relocateNodes(nonogram, columnsLegend, rowsLegend);
        pane.getChildren().add(nonogram);
        pane.getChildren().add(columnsLegend);
        pane.getChildren().add(rowsLegend);
        SceneManager.playAudio("audio/game_low.wav", -1);
    }

    private void relocateNodes(GridPane nonogram, GridPane columnsLegend, GridPane rowsLegend) {
        double stageWidth = SceneManager.getStageWidth();
        double stageHeight = SceneManager.getStageHeight();
        double nonogramColumnCount = nonogram.getColumnCount();
        double nonogramRowCount = nonogram.getRowCount();
        double columnsLegendRowCount = columnsLegend.getRowCount();
        double xOffsetNonogram = (nonogramColumnCount * 32) / 2;
        double yOffsetNonogram = (nonogramRowCount * 32) / 2;
        double yOffsetColumnsLegend = 20 * columnsLegendRowCount;
        double xOffsetRowsLegend = 32 * nonogramColumnCount;
        nonogram.relocate(stageWidth / 2 - xOffsetNonogram, stageHeight / 2 - yOffsetNonogram);
        columnsLegend.relocate(nonogram.getLayoutX(), nonogram.getLayoutY() - yOffsetColumnsLegend);
        rowsLegend.relocate(nonogram.getLayoutX() + xOffsetRowsLegend, nonogram.getLayoutY());
    }

    private GridPane getNonogram() {
        GridPane gp = new GridPane();
        gp.getStyleClass().add("nonogram");
        setupNonogram(gp);
        setupLogic();
        return gp;
    }

    private GridPane getColumnLegend() {
        GridPane gp = new GridPane();
        gp.getStyleClass().add("legend");
        int[][] columnsLegend = this.nonogram.getColumnsLegend();
        addConstraints(gp, columnsLegend.length, getBiggest(columnsLegend), 32, 20);
        int x = 0;
        for (int[] column : columnsLegend) {
            Label[] labels = new Label[column.length];
            for (int i = column.length - 1; i >= 0; i--) {
                labels[(column.length - 1) - i] = new Label(String.valueOf(column[i]));
            }
            gp.addColumn(x, labels);
            x++;
        }
        return gp;
    }

    private GridPane getRowLegend() {
        GridPane gp = new GridPane();
        gp.getStyleClass().add("legend");
        int[][] rowsLegend = this.nonogram.getRowsLegend();
        int y = 0;
        addConstraints(gp, getBiggest(rowsLegend), rowsLegend.length, 16, 32);
        for (int[] row : rowsLegend) {
            Label[] labels = new Label[row.length];
            for (int i = 0; i < row.length; i++) {
                labels[i] = new Label(String.valueOf(row[i]));
            }
            gp.addRow(y, labels);
            y++;
        }
        return gp;
    }

    private void setupNonogram(GridPane gp) {
        int width = this.nonogram.getColumns();
        int height = this.nonogram.getRows();
        addButtons(gp, width, height);
        addConstraints(gp, width, height);
    }

    private void addButtons(GridPane gp, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                System.out.println("adding button at [" + x + ", " + y + "]");
                this.tiles[y][x] = new Button();
                this.tiles[y][x].setId(x + ":" + y);
                gp.add(this.tiles[y][x], x, y);
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

    private void addConstraints(GridPane gp, int columns, int rows) {
        RowConstraints rowConstraint = new RowConstraints(32);
        ColumnConstraints columnConstraint = new ColumnConstraints(32);
        for (int x = 0; x < columns; x++) gp.getColumnConstraints().add(columnConstraint);
        for (int y = 0; y < rows; y++) gp.getRowConstraints().add(rowConstraint);
    }

    private void addConstraints(GridPane gp, int columns, int rows, int width, int height) {
        ColumnConstraints columnConstraint = new ColumnConstraints(width);
        RowConstraints rowConstraint = new RowConstraints(height);
        for (int x = 0; x < columns; x++) gp.getColumnConstraints().add(columnConstraint);
        for (int y = 0; y < rows; y++) gp.getRowConstraints().add(rowConstraint);
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

    private void setupLogic() {
        for (Button[] tileRow : this.tiles) {
            for (Button tile : tileRow) {
                tile.getStyleClass().clear();
                tile.getStyleClass().add("buttonBlank");
                tile.setOnMousePressed(mouseEvent -> {
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
        Parent original = this.getRoot();
        StackPane sp = new StackPane(original);
        ImageView image = new ImageView();
        image.getStyleClass().add("win");
        sp.getChildren().add(image);

        Timeline delayMenu = new Timeline(new KeyFrame(Duration.seconds(7.5), (actionEvent) -> {
            SceneManager.loadScene(new MainMenu());
        }));

        GaussianBlur gaussianBlur = new GaussianBlur();
        original.setEffect(gaussianBlur);
        gaussianBlur.setRadius(0);
        Timeline blurAnimation = new Timeline(new KeyFrame(Duration.millis(100), (actionEvent) -> {
            gaussianBlur.setRadius(gaussianBlur.getRadius() + 0.25);
        }));
        blurAnimation.setCycleCount(50);

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(image);
        fadeTransition.setDuration(Duration.seconds(0.2));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        Timeline makeTextAppear = new Timeline(new KeyFrame(Duration.seconds(4.3), (actionEvent) -> {
            fadeTransition.playFromStart();
        }));

        this.setRoot(sp);
        delayMenu.playFromStart();
        blurAnimation.playFromStart();
        makeTextAppear.playFromStart();
        SceneManager.playAudio(this.winAudio);
    }
}

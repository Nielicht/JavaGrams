package graphical;

import IO.FileSystem;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Nonogram;
import logic.Tiles;

public class GUIPlayer extends Application {
    private Nonogram nonogram;
    private Button[][] tiles;
    private Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logicSetup(primaryStage);
        this.mainStage.setTitle("Nonogram");
        this.mainStage.setResizable(false);
        this.mainStage.setWidth(400);
        this.mainStage.setHeight(400);
        this.mainStage.setScene(new Scene(setupGrid()));
        this.mainStage.show();
    }

    private void changeScene(Parent root) {
        this.mainStage.getScene().setRoot(root);
    }

    private void logicSetup(Stage primaryStage) {
        this.nonogram = new Nonogram(FileSystem.getResourceString("nFiles/test1.txt"));
        this.tiles = new Button[this.nonogram.getRows()][this.nonogram.getColumns()];
        this.mainStage = primaryStage;
    }

    private GridPane setupGrid() {
        GridPane gp = new GridPane();
        gp.getStylesheets().add("css/tiles.css");
        gp.getStyleClass().add("gridpane");
        poblateGrid(gp);
        setupButtons();
        System.out.println("Number of rows " + gp.getRowCount() + " ; Number of columns: " + gp.getColumnCount());
        return gp;
    }

    private void setupButtons() {
        for (Button[] tileRow : tiles) {
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
        BorderPane bp = new BorderPane(new Text("YOU WON!"));
        changeScene(bp);
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

    private void poblateGrid(GridPane gp) {
        int width = nonogram.getColumns();
        int height = nonogram.getRows();
        addButtons(gp, width, height);
        addConstraints(gp, width, height);
    }

    private void addButtons(GridPane gp, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.println("adding button at [" + x + ", " + y + "]");
                this.tiles[y][x] = new Button();
                this.tiles[y][x].setId(x + ":" + y);
                gp.add(this.tiles[y][x], x, y);
            }
        }
    }

    private void addConstraints(GridPane gp, int width, int height) {
        RowConstraints rowConstraint = new RowConstraints(32);
        ColumnConstraints columnConstraint = new ColumnConstraints(32);
        for (int x = 0; x < width; x++) gp.getColumnConstraints().add(columnConstraint);
        for (int y = 0; y < height; y++) gp.getRowConstraints().add(rowConstraint);
    }
}
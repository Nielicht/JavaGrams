package graphical;

import IO.FileSystem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import logic.Nonogram;

public class GUIPlayer extends Application {
    private Nonogram nonogram;
    private Button[][] tiles;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logicSetup();
        primaryStage.setTitle("Nonogram");
        primaryStage.setResizable(false);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        Scene scene = new Scene(setupGrid());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void logicSetup() {
        this.nonogram = new Nonogram(FileSystem.getResourceString("nFiles/test1.txt"));
        this.tiles = new Button[this.nonogram.getRows()][this.nonogram.getColumns()];
    }

    private GridPane setupGrid() {
        GridPane gp = new GridPane();
        gp.getStylesheets().add("css/tiles.css");
        gp.getStyleClass().add("gridpane");
        poblateGrid(gp);
        System.out.println("Number of rows " + gp.getRowCount() + " ; Number of columns: " + gp.getColumnCount());
        return gp;
    }

    private void poblateGrid(GridPane gp) {
        int width = nonogram.getColumns();
        int height = nonogram.getRows();
        addTiles(gp, width, height);
        addConstraints(gp, width, height);
    }

    private void addTiles(GridPane gp, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.println("adding button at [" + x + ", " + y + "]");
                this.tiles[y][x] = new Button();
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
package graphical;

import IO.FileSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.nio.file.Path;

public class MainMenu extends Scene {

    public MainMenu() {
        super(new VBox());
        this.getStylesheets().add("/css/selectScreen.css");
        this.getRoot().getStyleClass().add("vbox");
        addOptions();
        SceneManager.playAudio("/audio/menu.wav", -1);
    }

    private void addOptions() {
        VBox vBox = (VBox) this.getRoot();
        vBox.getChildren().clear();
        Button bronze = new Button("Bronze nonograms");
        bronze.getStyleClass().clear();
        bronze.getStyleClass().add("menuLabel");
        bronze.setStyle("-fx-text-fill: #6e3f0b");
        Button silver = new Button("Silver nonograms");
        silver.getStyleClass().clear();
        silver.getStyleClass().add("menuLabel");
        silver.setStyle("-fx-text-fill: silver");
        Button gold = new Button("Gold nonograms");
        gold.getStyleClass().clear();
        gold.getStyleClass().add("menuLabel");
        gold.setStyle("-fx-text-fill: gold");

        bronze.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/bronze", "/css/cosmosNonogram.css"));
        silver.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/silver", "/css/bronzeNonogram.css"));
        gold.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/gold", "/css/bronzeNonogram.css"));

        vBox.getChildren().addAll(gold, silver, bronze);
    }

    private void generateButtons(String relativePath, String skin) {
        VBox vBox = (VBox) this.getRoot();
        vBox.getChildren().clear();
        Path[] paths = FileSystem.getFPathsFromResourceFolder(relativePath);
        for (Path path : paths) {
            Button button = new Button(path.getFileName().toString());
            button.getStyleClass().clear();
            button.getStyleClass().add("buttons");
            button.setId(path.toString());
            setButtonLogic(button, skin);
            vBox.getChildren().add(button);
        }
    }

    private void setButtonLogic(Button button, String skin) {
        button.setOnAction((actionEvent) -> {
            SceneManager.playAudio("/audio/button.wav");
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.8), (actionEvent2) -> {
                SceneManager.loadScene(new Game(button.getId(), "/audio/win.wav", skin));
            }));
            tl.playFromStart();
        });
    }
}

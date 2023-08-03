package graphical;

import IO.FileSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Path;

public class MainMenu extends Scene {

    public MainMenu() {
        super(new VBox());
        this.getStylesheets().add("css/selectScreen.css");
        this.getRoot().getStyleClass().add("vbox");
        addOptions();
        SceneManager.playAudio("audio/menu.wav", -1);
    }

    private void addOptions() {
        VBox vBox = (VBox) this.getRoot();
        vBox.getChildren().clear();
        Button bronze = new Button("Bronze nonograms");
        bronze.getStyleClass().clear();
        bronze.setStyle("-fx-text-fill: #6e3f0b;-fx-font-family: alagard");
        Button silver = new Button("Silver nonograms");
        silver.getStyleClass().clear();
        silver.setStyle("-fx-text-fill: silver;-fx-font-family: alagard");
        Button gold = new Button("Gold nonograms");
        gold.getStyleClass().clear();
        gold.setStyle("-fx-text-fill: gold;-fx-font-family: alagard");

        bronze.setOnMousePressed(mouseEvent -> generateButtons("nFiles/bronze", "css/cosmosNonogram.css"));
        silver.setOnMousePressed(mouseEvent -> generateButtons("nFiles/silver", "css/bronzeNonogram.css"));
        gold.setOnMousePressed(mouseEvent -> generateButtons("nFiles/gold", "css/bronzeNonogram.css"));

        vBox.getChildren().addAll(gold, silver, bronze);
    }

    private void generateButtons(String path, String skin) {
        try {
            VBox vBox = (VBox) this.getRoot();
            vBox.getChildren().clear();
            Path[] files = FileSystem.getFPathsFromResourceFolder(path);
            for (Path file : files) {
                Button button = new Button(file.getFileName().toString());
                button.getStyleClass().clear();
                button.getStyleClass().add("buttons");
                button.setId(file.toString());
                setButtonLogic(button, skin);
                vBox.getChildren().add(button);
            }
        } catch (IOException ignored) {
            Platform.exit();
        }
    }

    private void setButtonLogic(Button button, String skin) {
        button.setOnAction((actionEvent) -> {
            SceneManager.playAudio("audio/button.wav");
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.8), (actionEvent2) -> {
                SceneManager.loadScene(new Game(button.getId(), "audio/win.wav", skin));
            }));
            tl.playFromStart();
        });
    }
}

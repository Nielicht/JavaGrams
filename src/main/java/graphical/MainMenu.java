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
        generateButtons();
    }

    private void generateButtons() {
        try {
            Path[] files = FileSystem.getFPathsFromResourceFolder("nFiles/");
            for (Path file : files) {
                Button button = new Button(file.getFileName().toString());
                button.getStyleClass().clear();
                button.getStyleClass().add("buttons");
                button.setId(file.toString());
                setButtonLogic(button);
                ((VBox) this.getRoot()).getChildren().add(button);
            }
        } catch (IOException ignored) {
            Platform.exit();
        }
    }

    private void setButtonLogic(Button button) {
        button.setOnAction((actionEvent) -> {
            GUIPlayer.playAudio("audio/button.wav");
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.8), (actionEvent2) -> {
                GUIPlayer.loadScene(new Game(button.getId()), "audio/game_low.wav");
            }));
            tl.playFromStart();
        });
    }
}

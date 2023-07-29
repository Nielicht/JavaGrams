package graphical;

import IO.FileSystem;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URISyntaxException;

public class SceneManager extends Application {
    private static Stage mainStage;
    private static MediaPlayer mp;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setup(primaryStage);
        loadScene(new MainMenu(), "audio/menu.wav");
        mainStage.show();
    }

    private static void setup(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setTitle("Nonogram");
        mainStage.setResizable(false);
        mainStage.setWidth(400);
        mainStage.setHeight(400);
    }

    public static void loadScene(Scene scene, double width, double height) {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equals("ESCAPE")) mainStage.close();
        });
        mainStage.setScene(scene);
        mainStage.setWidth(width);
        mainStage.setHeight(height);
        mainStage.centerOnScreen();
    }

    public static void loadScene(Scene scene) {
        loadScene(scene, mainStage.getWidth(), mainStage.getHeight());
    }

    public static void loadScene(Scene scene, String audio) {
        loadScene(scene);
        playAudio(audio);
    }

    public static void loadScene(Scene scene, double width, double height, String audio) {
        loadScene(scene, width, height);
        playAudio(audio);
    }

    public static void modifyScene(Parent root) {
        mainStage.getScene().setRoot(root);
    }

    public static void modifyScene(Parent root, int width, int height) {
        mainStage.setWidth(width);
        mainStage.setHeight(height);
        modifyScene(root);
    }

    public static void playAudio(String relativePath) {
        if (mp != null) mp.dispose();
        try {
            Media media = new Media(FileSystem.getResourceURI(relativePath).toString());
            mp = new MediaPlayer(media);
            mp.play();
        } catch (URISyntaxException ignored) {
        }
    }

    public static double getStageWidth() {
        return mainStage.getWidth();
    }

    public static double getStageHeight() {
        return mainStage.getHeight();
    }
}
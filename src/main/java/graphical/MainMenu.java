package graphical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Stage setup
        primaryStage.setTitle("JavaGrams");
        primaryStage.setResizable(false);

        // Scene setup
        Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(fxml);

        // Stage play
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
package graphical;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Stage setup
        primaryStage.setTitle("chirimoyo");
        primaryStage.setResizable(false);

        // Scene setup
        Group groupRootNode = new Group();
        Scene scene = new Scene(groupRootNode, 1280, 800, Color.BURLYWOOD);

        // Root node setup
        Line line = new Line();
        line.setStartX(70);
        line.setStartY(350);
        line.setEndX(200);
        line.setEndY(350);
        line.setStrokeWidth(7);
        line.setStroke(Color.MAGENTA);
        line.setRotate(45);
        groupRootNode.getChildren().add(line);

        Rectangle rectangle = new Rectangle();
        rectangle.setX(700);
        rectangle.setY(100);
        rectangle.setWidth(300);
        rectangle.setHeight(400);
        rectangle.setFill(Color.BLUE);
        rectangle.setStrokeWidth(5);
        rectangle.setStroke(Color.BLACK);
        groupRootNode.getChildren().add(rectangle);

        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                200.0, 200.0,
                500.0, 500.0,
                300.0, 150.0);
        triangle.setFill(Color.BROWN);
        groupRootNode.getChildren().add(triangle);

        Circle circle = new Circle();
        circle.setCenterX(500);
        circle.setCenterY(475);
        circle.setRadius(50);
        circle.setFill(Color.ORANGE);
        groupRootNode.getChildren().add(circle);

        Text text = new Text("This is not indeed centered");
        text.setX(scene.getWidth() / 2);
        text.setY(scene.getHeight() / 2);
        text.setFill(Color.RED);
        groupRootNode.getChildren().add(text);

        // Stage play
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
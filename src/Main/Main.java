package Main;

import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;



public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    /*@Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXMLs/firstMenu.fxml"));
        stage.setTitle("Bob Ross");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }*/

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720, Color.DARKSALMON);
        stage.setTitle("Bob Ross");

        Text text = new Text();
        text.setText("AAAAAAAAA");
        text.setX(120);
        text.setY(120);
        text.setFont(Font.font("Verdana", 50));
        text.setFill(Color.BLUE);

        Line line = new Line();
        line.setStartX(70);
        line.setStartY(350);
        line.setEndX(200);
        line.setEndY(350);
        line.setStrokeWidth(7);
        line.setStroke(Color.MAGENTA);
        line.setRotate(45);

        Rectangle rectangle = new Rectangle();
        rectangle.setX(700);
        rectangle.setY(100);
        rectangle.setWidth(300);
        rectangle.setHeight(400);
        rectangle.setFill(Color.BLUE);
        rectangle.setStrokeWidth(5);
        rectangle.setStroke(Color.BLACK);

        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                200.0, 200.0,
                500.0, 500.0,
                300.0, 150.0);

        triangle.setFill(Color.BROWN);

        Circle circle = new Circle();
        circle.setCenterX(500);
        circle.setCenterY(475);
        circle.setRadius(50);
        circle.setFill(Color.ORANGE);

        Image image = new Image("Resources/FatRat.png");
        ImageView imageView = new ImageView(image);
        imageView.setX(800);
        imageView.setY(500);

        root.getChildren().add(text);
        root.getChildren().add(line);
        root.getChildren().add(rectangle);
        root.getChildren().add(triangle);
        root.getChildren().add(circle);
        root.getChildren().add(imageView);
        stage.setScene(scene);
        stage.show();
    }
}

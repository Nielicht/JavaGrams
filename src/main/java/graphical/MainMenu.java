package graphical;

import IO.FileSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.nio.file.Path;

/**
 * This class represents the main menu of the game.
 */
public class MainMenu extends Scene {

    /**
     * Constructor for MainMenu.
     * Sets up the layout, styles, options, and background music for the main menu.
     */
    public MainMenu() {
        super(new VBox()); // Set up a vertical layout (Vertical Box) for the menu (sets the root node as the VBox)
        this.getStylesheets().add("/css/selectScreen.css"); // Load the global css stylesheet for the menu
        this.getRoot().getStyleClass().add("vbox"); // Use the "vbox" class from the stylesheet
        addOptions(); // Populate the menu with options
        SceneManager.playAudio("/audio/menu.wav", -1); // Plays background music at menu's launch
    }

    /**
     * Populates the main menu with options.
     */
    private void addOptions() {
        VBox vBox = (VBox) this.getRoot(); // Gets the root node of the scene to add options
        vBox.getChildren().clear(); // Empties the root node (VBox); not really required but just to be sure...

        // Creates buttons for each difficulty level
        Button bronze = new Button("Bronze nonograms"); // Generates a button with the specified label
        bronze.getStyleClass().clear(); // Removes the stock (and ugly) stylesheet
        bronze.getStyleClass().add("menuLabel"); // Use the "menuLabel" class from the stylesheet
        bronze.setStyle("-fx-text-fill: #6e3f0b"); // Sets the color to an attempt of a bronze color
        Button silver = new Button("Silver nonograms");
        silver.getStyleClass().clear();
        silver.getStyleClass().add("menuLabel");
        silver.setStyle("-fx-text-fill: silver");
        Button gold = new Button("Gold nonograms");
        gold.getStyleClass().clear();
        gold.getStyleClass().add("menuLabel");
        gold.setStyle("-fx-text-fill: gold");

        // Declares the actions taken when the buttons are pressed
        bronze.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/bronze", "/css/cosmosNonogram.css"));
        silver.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/silver", "/css/bronzeNonogram.css"));
        gold.setOnMousePressed(mouseEvent -> generateButtons("/nFiles/gold", "/css/bronzeNonogram.css"));

        // Add the buttons inside the main (root) Vertical Box Layout
        vBox.getChildren().addAll(gold, silver, bronze);
    }

    /**
     * Generates buttons for each nonogram file present to a given directory
     * @param relativePath The path to the nonogram files, relative to the resources folder
     * @param skin The visual style nonograms from this category will take
     */
    private void generateButtons(String relativePath, String skin) {
        VBox vBox = (VBox) this.getRoot(); // Gets the root node of the scene to add options
        vBox.getChildren().clear(); // Empties the already present elements

        Path[] paths = FileSystem.getFPathsFromResourceFolder(relativePath); // Generates a list of paths from the files
        for (Path path : paths) { // For each path...
            Button button = new Button(path.getFileName().toString()); // Creates a button with the filename as the button's label
            button.getStyleClass().clear(); // Removes the stock (and ugly) stylesheet
            button.getStyleClass().add("buttons"); // Use the "buttons" class from the stylesheet
            button.setId(path.toString()); // Use the path as the button's ID, will be used at a later stage

            setButtonLogic(button, skin); // Define what happens when the button is clicked
            vBox.getChildren().add(button); // Add the nonogram button to the menu
        }
    }

    /**
     * Defines what happens when a nonogram button is clicked.
     * @param button The nonogram button
     * @param skin The visual style for the nonogram game
     */
    private void setButtonLogic(Button button, String skin) {
        button.setOnAction((actionEvent) -> { // When the button is clicked...
            SceneManager.playAudio("/audio/button.wav"); // Play a sound effect
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.8), (actionEvent2) -> {
                SceneManager.loadScene(new Game(button.getId(), "/audio/win.wav", skin)); // Start the game
            }));
            tl.playFromStart(); // Begin the transition to the game
        });
    }
}

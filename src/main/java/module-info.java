module com.github.nielicht.javagrams {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.github.nielicht.javagrams to javafx.fxml;
    exports com.github.nielicht.javagrams;
}
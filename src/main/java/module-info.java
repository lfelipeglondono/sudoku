/**
 * Module declaration for the application, specifying its dependencies and exported packages.
 */
module com.example.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.sudoku.controllers to javafx.fxml;
    exports com.example.sudoku;
    exports com.example.sudoku.models;
    exports com.example.sudoku.controllers;
    exports com.example.sudoku.views;
    exports com.example.sudoku.views.alert;
}
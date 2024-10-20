package com.example.sudoku;

import com.example.sudoku.views.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the main class of the program.
 * @author Felipe Garcia
 * @version 1.0
 */
public class Main extends Application {
    /**
     * Main method, where the execution of the program begins.
     * @param args Arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GameView.getInstance();
    }
}

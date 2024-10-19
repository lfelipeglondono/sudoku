package com.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class generates the view of the Start.
 * @author Felipe Garcia
 * @version 1.0
 */
public class StartView extends Stage {

    /**
     * This method creates the window and assigns the Start scene to it.
     * @throws IOException if an error occurs during the creation of the StartView
     */
    public StartView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/sudoku/start-view.fxml")
        );
        Parent root = loader.load();
        this.setTitle("Sudoku - Start");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/sudoku/images/favicon.png")
        ));
        this.setScene(scene);
        this.show();
    }

    /**
     * Retrieves the instance of StartView.
     * If the instance does not exist, it creates a new one.
     * @return instance of StartViewHolder
     * @throws IOException if an error occurs during the creation of the StartView
     */
    public static StartView getInstance() throws IOException {
        if (StartViewHolder.INSTANCE == null) {
            return StartViewHolder.INSTANCE = new StartView();
        } else {
            return StartViewHolder.INSTANCE;
        }
    }

    /**
     * Holder class for the instance of StartView.
     */
    private static class StartViewHolder {
        private static StartView INSTANCE;
    }
}
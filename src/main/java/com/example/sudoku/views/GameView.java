package com.example.sudoku.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class generates the view of the Game.
 * @author Felipe Garcia
 * @version 1.0
 */
public class GameView extends Stage {
    /**
     * This method creates the window and assigns the Game scene to it.
     * @throws IOException if an error occurs during the creation of the Stage
     */
    public GameView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/sudoku/game-view.fxml")
        );
        Parent root = loader.load();
        this.setTitle("Sudoku - Play");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/sudoku/images/favicon.png")
        ));
        scene.getStylesheets().add(getClass().getResource("/com/example/sudoku/styles.css").toExternalForm());
        this.setScene(scene);
        this.show();
    }

    /**
     * Retrieves the instance of GameView.
     * If the instance does not exist, it creates a new one.
     * @return The instance of GameView.
     * @throws IOException if an error occurs during the creation of the GameView
     */
    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            return GameViewHolder.INSTANCE = new GameView();
        }
        else {
            return GameViewHolder.INSTANCE;
        }
    }

    /**
     * Holder class for the instance of GameView.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }
}

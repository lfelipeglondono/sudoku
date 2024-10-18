package com.example.sudoku.controllers;
import com.example.sudoku.views.GameView;
import com.example.sudoku.views.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController  {
    @FXML
    void onHandlePlayButton(ActionEvent event) throws IOException {
        AlertBox alertBox = new AlertBox();
        boolean decision = alertBox.showConfirmation("Sudoku - Game Start Confirmation", "Do you want to start playing?", "Click \"OK\" to start playing.");

        if (decision) {
            GameView.getInstance();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}

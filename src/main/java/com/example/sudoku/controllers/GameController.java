package com.example.sudoku.controllers;

import com.example.sudoku.models.Sudoku;
import com.example.sudoku.views.alert.AlertBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.UnaryOperator;

/**
 * This class is the controller for the Game window.
 * @author Felipe Garcia
 * @version 1.0
 */
public class GameController {
    @FXML
    private GridPane sudoku;
    private final ArrayList<ArrayList<TextField>> sudokuFields = new ArrayList<>();
    private Sudoku sudokuGame;
    private final AlertBox alertBox = new AlertBox();

    /**
     * This method generates the Sudoku cells, assigning a format to them, and handles events in case something is written.
     */
    public void initialize() {
        for (int row = 0; row < 6; row++) {
            sudokuFields.add(new ArrayList<>());
            for (int col = 0; col < 6; col++) {
                TextField currentTextField = new TextField();
                sudokuFields.get(row).add(currentTextField);

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d?")) {
                        return change;
                    }
                    return null;
                };

                currentTextField.setTextFormatter(new TextFormatter<>(filter));
                int finalRow = row;
                int finalCol = col;
                currentTextField.textProperty().addListener((observable, oldValue, newValue) -> handleInputField(newValue, finalRow, finalCol));
                currentTextField.getStyleClass().add("cell");
                currentTextField.setMinHeight(40);
                currentTextField.setMinWidth(40);
                sudoku.add(currentTextField, col, row);

                if ((row == 1 || row == 3) && col == 2) {
                    currentTextField.getStyleClass().add("cell-right-bottom");
                } else if (col == 2) {
                    currentTextField.getStyleClass().add("cell-right");
                } else if (row == 1 || row == 3) {
                    currentTextField.getStyleClass().add("cell-bottom");
                }
            }
        }

        sudokuGame = new Sudoku(sudokuFields);
        sudokuGame.fillRandomBoxes();
    }

    /**
     * This method allows for suggesting hints to fill in the Sudoku.
     */
    @FXML
    void onHandleHelpButton() {
        TextField textField;
        Random rand = new Random();
        int n, row, col;

        while (sudokuGame.checkThereNumberValid()) {
            n = rand.nextInt(6) + 1;
            row = rand.nextInt(6);
            col = rand.nextInt(6);

            textField = sudokuFields.get(row).get(col);

            if (!textField.getText().isEmpty()) {
                continue;
            }

            if (sudokuGame.validateNumber(row, col, n)) {
                textField.setStyle("-fx-prompt-text-fill: yellow; -fx-border-color: #9a4d03;");
                textField.setPromptText(String.valueOf(n));
                return;
            }
        }

        alertBox.showAlert("Sudoku - Information", "Ops!", "There are no numbers available to suggest to you.");
    }

    /**
     * This method controls the input in the Sudoku cells and checks if the number is valid.
     * @param newValue It's the new number entered.
     * @param row Row where the number was entered.
     * @param col Column where the number was entered.
     */
    public void handleInputField(String newValue, int row, int col) {
        TextField textField = sudokuFields.get(row).get(col);

        int newValueInt;

        textField.setStyle("-fx-border-color: #9a4d03; -fx-prompt-text-fill: green;");

        if (!newValue.isEmpty()) {
            textField.setPromptText("");

            try {
                newValueInt = Integer.parseInt(newValue);

                if (newValueInt < 0 || newValueInt > 6) {
                    throw new IllegalArgumentException("You entered a number that is not in the range of 1 to 6.");
                }

                if (!sudokuGame.validateNumber(row, col, newValueInt)) {
                    textField.setPromptText(String.valueOf(newValueInt));
                    throw new Exception("The number in that position is invalid. Number: " + newValueInt);
                }

                if (sudokuGame.win()) {
                    alertBox.showAlert("Sudoku - Win", "Congratulations!", "You won! You completed the Sudoku.");
                }

            } catch (IllegalArgumentException e) {
                textField.setText("");
                alertBox.showAlert("Sudoku - Error", "You must enter only numbers from 1 to 6.", e.getMessage());
            } catch (Exception e) {
                textField.setText("");
                textField.setStyle("-fx-border-color: red; -fx-prompt-text-fill: red;");
                alertBox.showAlert("Sudoku - Error", "Something went wrong", e.getMessage());
            }
        }
    }
}

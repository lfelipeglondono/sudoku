package com.example.sudoku.controllers;

import com.example.sudoku.views.alert.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.UnaryOperator;

public class GameController {
    @FXML
    private GridPane sudoku;
    ArrayList<ArrayList<TextField>> sudokuFields = new ArrayList<>();

    public void initialize() {
        TextField currentTextField;

        for (int row = 0; row < 6; row++) {
            sudokuFields.add(new ArrayList<>());

            for (int col = 0; col < 6; col++) {
                sudokuFields.get(row).add(new TextField());
                int finalRow = row;
                int finalCol = col;

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d?")) { // Permitir solo un dígito
                        return change;
                    }
                    return null;
                };

                currentTextField = sudokuFields.get(row).get(col);
                currentTextField.setTextFormatter(new TextFormatter<>(filter));
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

        fillRandomBoxes();
    }

    public void fillRandomBoxes() {
        Random random = new Random();

        for (int boxRow = 0; boxRow < 6; boxRow += 2) {
            for (int boxCol = 0; boxCol < 6; boxCol += 3) {
                int filled = 0;

                while (filled < 2) {
                    int row = boxRow + random.nextInt(2);
                    int col = boxCol + random.nextInt(3);
                    int randomValue = 1 + random.nextInt(6);

                    TextField textField = sudokuFields.get(row).get(col);
                    if (textField.getText().isEmpty() && validateNumber(row, col, randomValue)) {
                        textField.setText(String.valueOf(randomValue));
                        textField.setEditable(false);
                        textField.setStyle("-fx-background-color: #e1b884");
                        filled++;
                    }
                }
            }
        }
    }

    public boolean validateNumber(int row, int col, int n) {
        TextField textField;

        for (int i = 0; i < 6; i++) {
            textField = sudokuFields.get(i).get(col);
            if (!textField.getText().isEmpty() && Integer.parseInt(textField.getText()) == n && i != row) {
                return false;
            }
        }

        for (int j = 0; j < 6; j++) {
            textField = sudokuFields.get(row).get(j);
            if (!textField.getText().isEmpty() && Integer.parseInt(textField.getText()) == n && j != col) {
                return false;
            }
        }

        int boxRowStart = (row / 2) * 2;
        int boxColStart = (col / 3) * 3;

        for (int i = boxRowStart; i < boxRowStart + 2; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                textField = sudokuFields.get(i).get(j);

                if (!textField.getText().isEmpty() && Integer.parseInt(textField.getText()) == n && !(i == row && j == col)) {
                    return false;
                }
            }
        }

        return true;
    }

    @FXML
    void onHandleHelpButton(ActionEvent event) {
        TextField textField;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                textField = sudokuFields.get(i).get(j);
                int validNumber = findValidNumber(i, j);

                if (!textField.getText().isEmpty()) {
                    continue;
                }

                if (validNumber != -1) {
                    textField.setStyle("-fx-prompt-text-fill: yellow; -fx-border-color: #9a4d03;");
                    textField.setPromptText(String.valueOf(validNumber));
                    return;
                }
            }
        }
    }

    private int findValidNumber(int row, int col) {
        for (int n = 1; n <= 6; n++) {
            if (validateNumber(row, col, n)) {
                return n;
            }
        }
        return -1;
    }

    public void handleInputField(String newValue, int row, int col) {
        AlertBox alertBox = new AlertBox();
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

                if (!validateNumber(row, col, newValueInt)) {
                    textField.setPromptText(String.valueOf(newValueInt));
                    throw new Exception("The number in that position is invalid. Number: " + String.valueOf(newValueInt));
                }
            } catch (NumberFormatException e) {
                System.out.println("You must enter a number.");
                textField.setText("");
                alertBox.showAlert("Sudoku - Error", "You must enter only numbers from 1 to 6.", e.getMessage());
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

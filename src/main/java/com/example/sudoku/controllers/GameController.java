package com.example.sudoku.controllers;

import com.example.sudoku.models.Sudoku;
import com.example.sudoku.views.GameView;
import com.example.sudoku.views.alert.AlertBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
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
    private final ArrayList<ArrayList<TextField>> sudokuBoard = new ArrayList<>();
    private Sudoku sudokuGame;
    private final AlertBox alertBox = new AlertBox();
    private final Random rand = new Random();

    private static final int SIZE = 6;
    private static final int SUBGRID_ROWS = 2;
    private static final int SUBGRID_COLS = 3;

    /**
     * This method initializes the Sudoku board and UI components.
     */
    public void initialize() {
        setupSudokuBoard();
        sudokuGame = new Sudoku();
        showInitialSudoku();
    }

    private void setupSudokuBoard() {
        for (int row = 0; row < SIZE; row++) {
            sudokuBoard.add(new ArrayList<>());
            for (int col = 0; col < SIZE; col++) {
                TextField textField = createSudokuCell(row, col);
                sudoku.add(textField, col, row);
            }
        }
    }

    private TextField createSudokuCell(int row, int col) {
        TextField textField = new TextField();
        sudokuBoard.get(row).add(textField);
        setupTextFieldFormatter(textField);
        setupCellHighlighting(textField, row, col);
        styleCell(textField, row, col);
        return textField;
    }

    private void setupTextFieldFormatter(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[1-6]?") ? change : null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void setupCellHighlighting(TextField textField, int row, int col) {
        textField.textProperty().addListener((observable, oldValue, newValue) ->
                handleInputField(newValue, row, col));

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                highlightCells(row, col);
            } else {
                clearHighlight();
            }
        });
    }

    private void styleCell(TextField textField, int row, int col) {
        textField.getStyleClass().add("cell");

        if ((row == 1 || row == 3) && col == 2) {
            textField.getStyleClass().add("cell-right-bottom");
        } else if (col == 2) {
            textField.getStyleClass().add("cell-right");
        } else if (row == 1 || row == 3) {
            textField.getStyleClass().add("cell-bottom");
        }
    }

    private void highlightCells(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                sudokuBoard.get(row).get(i).getStyleClass().add("others-highlight");
            }
            if (i != row) {
                sudokuBoard.get(i).get(col).getStyleClass().add("others-highlight");
            }
        }
        sudokuBoard.get(row).get(col).getStyleClass().add("highlight");
    }

    private void clearHighlight() {
        for (ArrayList<TextField> row : sudokuBoard) {
            for (TextField cell : row) {
                cell.getStyleClass().remove("highlight");
                cell.getStyleClass().remove("others-highlight");
            }
        }
    }

    public void showInitialSudoku() {
        for (int boxRow = 0; boxRow < SIZE; boxRow += SUBGRID_ROWS) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += SUBGRID_COLS) {
                fillSubgrid(boxRow, boxCol);
            }
        }
    }

    private void fillSubgrid(int boxRow, int boxCol) {
        int filled = 0;

        while (filled < 2) {
            int row = boxRow + rand.nextInt(SUBGRID_ROWS);
            int col = boxCol + rand.nextInt(SUBGRID_COLS);

            TextField textField = sudokuBoard.get(row).get(col);
            if (textField.getText().isEmpty()) {
                int numberCell = sudokuGame.getNumber(row, col);
                textField.getStyleClass().add("pinned-cell");
                textField.setText(String.valueOf(numberCell));
                textField.setEditable(false);
                filled++;
            }
        }
    }

    @FXML
    void onHandleHintButton() {
        if (win()) {
            alertBox.showAlert(Alert.AlertType.ERROR, "Sudoku - Information", "Ops!", "There are no numbers available to suggest to you.");
            return;
        }

        TextField textField;
        int row, col;

        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
            textField = sudokuBoard.get(row).get(col);
        } while (!textField.getText().isEmpty());

        textField.getStyleClass().remove("error-cell");
        textField.getStyleClass().add("hint-cell");
        textField.setPromptText(String.valueOf(sudokuGame.getNumber(row, col)));
    }

    public boolean win() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                TextField textField = sudokuBoard.get(j).get(i);
                if (textField.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    void onHandleHelpButton() {
        alertBox.showAlert(Alert.AlertType.INFORMATION, "Sudoku - Help", "Instructions", """
                Objective: Fill the grid so that each row, column, and 2x3 box contains the numbers 1 to 6 without repetition.
                
                Rules:
                Each number (1-6) must appear exactly once in each row.
                Each number (1-6) must appear exactly once in each column.
                Each number (1-6) must appear exactly once in each 2x3 box.
                
                Winning the Game: The game is complete when the entire grid is filled correctly following the rules.""");
    }

    @FXML
    void onHandleRestartButton() throws IOException {
        boolean decision = alertBox.showAlert(Alert.AlertType.CONFIRMATION, "Sudoku - Game Start Confirmation", "Do you want to start playing?", "Click \"OK\" to start playing.");

        if (decision) {
            GameView.getInstance();

            Stage stage = (Stage) sudoku.getScene().getWindow();
            stage.close();

            new GameView();
        }
    }

    public void handleInputField(String newValue, int row, int col) {
        TextField textField = sudokuBoard.get(row).get(col);

        if (!newValue.isEmpty()) {
            try {
                int newValueInt = Integer.parseInt(newValue);
                validateInput(textField, newValueInt, row, col);
            } catch (IllegalArgumentException e) {
                handleInvalidInput(textField, newValue);
            }
        }
    }

    private void validateInput(TextField textField, int newValueInt, int row, int col) {
        textField.getStyleClass().remove("hint-cell");
        textField.getStyleClass().remove("error-cell");

        if (sudokuGame.getNumber(row, col) != newValueInt) {
            throw new IllegalArgumentException("The number in that position is invalid. Number: " + newValueInt);
        }

        textField.setPromptText("");

        if (win()) {
            alertBox.showAlert(Alert.AlertType.INFORMATION, "Sudoku - Win", "Congratulations!", "You won! You completed the Sudoku.");
        }
    }

    private void handleInvalidInput(TextField textField, String newValue) {
        textField.setText("");
        textField.setPromptText(newValue);
        textField.getStyleClass().add("error-cell");
        alertBox.showAlert(Alert.AlertType.ERROR, "Sudoku - Error", "Something went wrong", "Invalid input: " + newValue);
    }
}

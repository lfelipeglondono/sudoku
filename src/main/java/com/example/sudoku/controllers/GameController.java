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

    /**
     * Sets up the Sudoku board by creating a TextField for each cell.
     */
    private void setupSudokuBoard() {
        for (int row = 0; row < SIZE; row++) {
            sudokuBoard.add(new ArrayList<>());
            for (int col = 0; col < SIZE; col++) {
                TextField textField = createSudokuCell(row, col);
                sudoku.add(textField, col, row);
            }
        }
    }

    /**
     * Creates a Sudoku cell.
     *
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return A TextField for the cell.
     */
    private TextField createSudokuCell(int row, int col) {
        TextField textField = new TextField();
        sudokuBoard.get(row).add(textField);
        setupTextFieldFormatter(textField);
        setupCellHighlighting(textField, row, col);
        styleCell(textField, row, col);
        return textField;
    }

    /**
     * Sets up the formatter for the TextField to allow only numbers from 1 to 6.
     *
     * @param textField The TextField to format.
     */
    private void setupTextFieldFormatter(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("[1-6]?") ? change : null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * Sets up cell highlighting when interacting with the TextField.
     *
     * @param textField The TextField to set up.
     * @param row       Row of the cell.
     * @param col       Column of the cell.
     */
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

    /**
     * Styles a cell based on its position.
     *
     * @param textField The TextField to style.
     * @param row       Row of the cell.
     * @param col       Column of the cell.
     */
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

    /**
     * Highlights the cells in the row and column of the currently focused cell.
     *
     * @param row Row of the cell.
     * @param col Column of the cell.
     */
    private void highlightCells(int row, int col) {
        int subgridStartRow = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int subgridStartCol = (col / SUBGRID_COLS) * SUBGRID_COLS;

        for (int i = 0; i < SIZE; i++) {
            if (i != col) {
                sudokuBoard.get(row).get(i).getStyleClass().add("others-highlight");
            }
            if (i != row) {
                sudokuBoard.get(i).get(col).getStyleClass().add("others-highlight");
            }
        }

        for (int r = subgridStartRow; r < subgridStartRow + SUBGRID_ROWS; r++) {
            for (int c = subgridStartCol; c < subgridStartCol + SUBGRID_COLS; c++) {
                sudokuBoard.get(r).get(c).getStyleClass().add("others-highlight");
            }
        }

        sudokuBoard.get(row).get(col).getStyleClass().add("highlight");
    }

    /**
     * Clears the highlight from all cells.
     */
    private void clearHighlight() {
        for (ArrayList<TextField> row : sudokuBoard) {
            for (TextField cell : row) {
                cell.getStyleClass().remove("highlight");
                cell.getStyleClass().remove("others-highlight");
                cell.getStyleClass().remove("others-highlight");
            }
        }
    }

    /**
     * Shows the initial Sudoku by filling in some numbers on the board.
     */
    public void showInitialSudoku() {
        for (int boxRow = 0; boxRow < SIZE; boxRow += SUBGRID_ROWS) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += SUBGRID_COLS) {
                fillSubgrid(boxRow, boxCol);
            }
        }
    }

    /**
     * Fills a 2x3 subgrid with numbers.
     * @param boxRow Starting row of the subgrid.
     * @param boxCol Starting column of the subgrid.
     */
    private void fillSubgrid(int boxRow, int boxCol) {
        TextField textField;
        int filled = 0;

        while (filled < 2) {
            int row = boxRow + rand.nextInt(SUBGRID_ROWS);
            int col = boxCol + rand.nextInt(SUBGRID_COLS);

            textField = sudokuBoard.get(row).get(col);
            if (textField.getText().isEmpty()) {
                int numberCell = sudokuGame.getNumber(row, col);
                textField.getStyleClass().add("pinned-cell");
                textField.setText(String.valueOf(numberCell));
                textField.setEditable(false);
                filled++;
            }
        }
    }

    /**
     * Handles the event when the hint button is pressed.
     */
    @FXML
    void onHandleHintButton() {
        TextField textField;
        int row, col;

        if (win()) {
            alertBox.showAlert(Alert.AlertType.ERROR, "Sudoku - Information", "Ops!", "There are no numbers available to suggest to you.");
            return;
        }

        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
            textField = sudokuBoard.get(row).get(col);
        } while (!textField.getText().isEmpty());

        textField.getStyleClass().remove("error-cell");
        textField.getStyleClass().add("hint-cell");
        textField.setPromptText(String.valueOf(sudokuGame.getNumber(row, col)));
    }

    /**
     * Checks if the game has been won.
     * @return true if the game is won, false otherwise.
     */
    public boolean win() {
        TextField textField;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                textField = sudokuBoard.get(j).get(i);
                if (textField.getText().isEmpty()) {
                    return false;
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                textField = sudokuBoard.get(i).get(j);

                textField.setEditable(false);
            }
        }

        return true;
    }

    /**
     * Handles the event when the help button is pressed.
     */
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

    /**
     * Handles the event when the restart button is pressed.
     * Prompts the user for confirmation to restart the game.
     * If confirmed, it closes the current game window and starts a new game.
     *
     * @throws IOException If an I/O error occurs during the restart process.
     */
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

    /**
     * Handles input changes in the Sudoku cells.
     * Validates the new value entered in a cell.
     *
     * @param newValue The new value entered by the user.
     * @param row The row of the cell being modified.
     * @param col The column of the cell being modified.
     */
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

    /**
     * Validates the input value for a specific cell.
     * Checks if the input matches the expected value from the Sudoku game.
     * If the game is won, shows a congratulatory message.
     *
     * @param textField The TextField being validated.
     * @param newValueInt The new integer value entered.
     * @param row The row of the cell.
     * @param col The column of the cell.
     */
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

    /**
     * Handles invalid input by clearing the TextField and showing an error alert.
     *
     * @param textField The TextField with invalid input.
     * @param newValue The invalid value entered by the user.
     */
    private void handleInvalidInput(TextField textField, String newValue) {
        textField.setText("");
        textField.setPromptText(newValue);
        textField.getStyleClass().add("error-cell");
        alertBox.showAlert(Alert.AlertType.ERROR, "Sudoku - Error", "Something went wrong", "Invalid input: " + newValue);
    }
}

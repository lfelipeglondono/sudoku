package com.example.sudoku.models;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is from the Sudoku model.
 *  @author Felipe Garcia
 *  @version 1.0
 */
public class Sudoku {
    private final ArrayList<ArrayList<TextField>> sudokuFields;

    /**
     * This method is the constructor of the class; it is initialized with the array of text fields (Sudoku fields).
     * @param sudokuFields sudoku fields
     */
    public Sudoku(ArrayList<ArrayList<TextField>> sudokuFields) {
        this.sudokuFields = sudokuFields;
    }

    /**
     * This method initially fills the Sudoku with 2 numbers per box.
     */
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

    /**
     * This method validates that the entered number is valid.
     * @param row Row where the number will be entered.
     * @param col Column where the number will be entered.
     * @param n Number that will be entered.
     * @return A boolean indicating whether the number exists or not.
     */
    public boolean validateNumber(int row, int col, int n) {
        TextField textField;

        for (int i = 0; i < 6; i++) {
            textField = sudokuFields.get(i).get(col);
            if (i != row) {
                if (!textField.getText().isEmpty() && Integer.parseInt(textField.getText()) == n) {
                    return false;
                }
            }

            textField = sudokuFields.get(row).get(i);

            if (i != col) {
                if (!textField.getText().isEmpty() && Integer.parseInt(textField.getText()) == n) {
                    return false;
                }
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

    /**
     * This method checks if a number can be entered in any cell.
     * @return A boolean checking if any cell can take a number.
     */
    public boolean checkThereNumberValid() {
        TextField textField;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                textField = sudokuFields.get(i).get(j);
                int validNumber = findValidNumber(i, j);

                if (!textField.getText().isEmpty()) {
                    continue;
                }

                if (validNumber != -1) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This number searches for valid numbers in the position where it is entered.
     * @param row Row where the number will be filled in.
     * @param col Column where the number will be filled in.
     * @return A valid number in that position; if none are valid, it returns -1.
     */

    public int findValidNumber(int row, int col) {
        for (int n = 1; n <= 6; n++) {
            if (validateNumber(row, col, n)) {
                return n;
            }
        }
        return -1;
    }

    /**
     * This method checks that all the cells have a value; since they were valid, it means that you won.
     * @return Boolean checking if you won.
     */
    public boolean win() {
        TextField textField;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                textField = sudokuFields.get(row).get(col);

                if (textField.getText().isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }
}
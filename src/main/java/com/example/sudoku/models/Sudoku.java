package com.example.sudoku.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is from the Sudoku model.
 *  @author Felipe Garcia
 *  @version 1.0
 */
public class Sudoku {
    private final int SIZE = 6;
    private final int SUBGRID_ROWS = 2;
    private final int SUBGRID_COLS = 3;

    private final ArrayList<ArrayList<Integer>> board;

    /**
     * Constructor for the Sudoku class.
     * Initializes the board with zeros and generates a valid Sudoku puzzle.
     */
    public Sudoku() {
        board = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            board.add(new ArrayList<>());

            for (int j = 0; j < SIZE; j++) {
                board.get(i).add(0);
            }
        }

        while (!generateSudoku());
    }

    /**
     * Generates a valid Sudoku puzzle by filling in the diagonal subgrids and then filling the remaining cells.
     * @return true if the Sudoku puzzle was generated successfully, false otherwise.
     */
    public boolean generateSudoku() {
        fillDiagonal();
        return fillRemaining(0, 3);
    }

    /**
     * Fills the diagonal subgrids with random numbers ensuring they are valid.
     */
    private void fillDiagonal() {
        int number;
        Random rand = new Random();

        for (int row = 0; row < SUBGRID_ROWS; row++) {
            for (int col = 0; col < SUBGRID_COLS; col++) {

                do {
                    number = rand.nextInt(SIZE) + 1;
                } while (!isValid(row, col, number));

                board.get(row).set(col, number);

                do {
                    number = rand.nextInt(SIZE) + 1;
                } while (!isValid(row + 2, col + 3, number));

                board.get(row + 2).set(col + 3, number);
            }
        }
    }

    /**
     * Fills the remaining cells of the Sudoku board.
     * @param row The current row to fill.
     * @param col The current column to fill.
     * @return true if all cells are filled correctly, false otherwise.
     */
    private boolean fillRemaining(int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillRemaining(row + 1, 0);
        if (board.get(row).get(col) != 0) return fillRemaining(row, col + 1);

        for (int k = 0; k <= SIZE; k++) {
            if (isValid(row, col, k)) {
                board.get(row).set(col, k);
                if (fillRemaining(row, col + 1)) return true;
                board.get(row).set(col, 0);
            }
        }

        return false;
    }

    /**
     * Checks if placing a number in a given cell is valid.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @param n The number to be placed in the cell.
     * @return true if the number can be placed, false otherwise.
     */
    public boolean isValid(int row, int col, int n) {
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == n) {
                return false;
            }

            if (board.get(row).get(i) == n) {
                return false;
            }
        }

        int boxRowStart = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int boxColStart = (col / SUBGRID_COLS) * SUBGRID_COLS;
        int valueCell;

        for (int i = boxRowStart; i < boxRowStart + SUBGRID_ROWS; i++) {
            for (int j = boxColStart; j < boxColStart + SUBGRID_COLS; j++) {
                valueCell = board.get(i).get(j);
                if (valueCell == n) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Retrieves the number in the specified cell.
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return The number in the cell at the specified row and column.
     */
    public int getNumber(int row, int col) {
        return board.get(row).get(col);
    }
}
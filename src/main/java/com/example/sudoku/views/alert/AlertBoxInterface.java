package com.example.sudoku.views.alert;

/**
 * This interface is used to implement it in the AlertBox.
 * @author Felipe Garcia
 * @version 1.0
 */
public interface AlertBoxInterface {
    /**
     * This method generates an informational alert.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     */
    void showAlert(String title, String header, String message);

    /**
     * This method generates a confirmation alert.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     * @return A boolean, depending on whether the person accepted or not.
     */
    boolean showConfirmation(String title, String header, String message);
}

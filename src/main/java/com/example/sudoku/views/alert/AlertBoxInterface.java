package com.example.sudoku.views.alert;

import javafx.scene.control.Alert;

/**
 * This interface is used to implement it in the AlertBox.
 * @author Felipe Garcia
 * @version 1.0
 */
public interface AlertBoxInterface {
    /**
     * This method generates an informational alert.
     * @param alertType type of window.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     * @return decision of user if the window is the type confirmation
     */
    boolean showAlert(Alert.AlertType alertType, String title, String header, String message);
}

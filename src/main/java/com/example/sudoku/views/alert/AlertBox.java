package com.example.sudoku.views.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Class to handle alerts.
 * @author Felipe Garcia
 * @version 1.0
 */
public class AlertBox implements AlertBoxInterface {

    /**
     * This method generates an informational alert.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     */
    public void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method generates a confirmation alert.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     * @return A boolean, depending on whether the person accepted or not.
     */
    @Override
    public boolean showConfirmation(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}

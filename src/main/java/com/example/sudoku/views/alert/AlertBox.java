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
     * @param alertType Type of window.
     * @param title Title of the window.
     * @param header Large title.
     * @param message Message of the information to be conveyed.
     * @return decision of user if the window is the type confirmation
     */
    public boolean showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}

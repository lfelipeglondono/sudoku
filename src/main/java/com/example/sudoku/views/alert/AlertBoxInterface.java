package com.example.sudoku.views.alert;

public interface AlertBoxInterface {
    void showAlert(String title, String header, String message);
    boolean showConfirmation(String title, String header, String message);
}

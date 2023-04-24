package com.example.gymmanagementsystem.dependencies;

import javafx.scene.control.Alert;

public class Alerts {

    private static Alert notificationAlert;
    private static Alert warningAlert;
    private static Alert errorAlert;

    public static Alert notificationAlert(String message, String title) {
        if (notificationAlert != null) return notificationAlert;
        notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle(title);
        notificationAlert.setContentText(message);
        notificationAlert.showAndWait();

        return notificationAlert;

    }
    public static Alert waningAlert(String message, String title) {
        if (warningAlert != null) return warningAlert;
        warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle(title);
        warningAlert.setContentText(message);
        warningAlert.showAndWait();
        return warningAlert;
    }
    public static Alert errorAlert(String message, String title) {
        if (errorAlert != null) return errorAlert;
        errorAlert = new Alert(Alert.AlertType.WARNING);
        errorAlert.setTitle(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
        return errorAlert;
    }



}

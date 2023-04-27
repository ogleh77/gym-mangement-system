package com.example.gymmanagementsystem.dependencies;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    protected final static ButtonType ok = new ButtonType("Haa");
    protected final static ButtonType no = new ButtonType("Maya");

    public static Alert notificationAlert(String message, String title) {
        Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle(title);
        notificationAlert.setContentText(message);
        notificationAlert.showAndWait();
        return notificationAlert;
    }

    public static Alert waningAlert(String message, String title) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle(title);
        warningAlert.setContentText(message);
        warningAlert.showAndWait();
        return warningAlert;
    }

    public static void errorAlert(String message, String title) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }


    public static boolean confirmationAlert(String message, String title) {
        Optional<ButtonType> result;

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, message, no, ok);
        confirmationAlert.setTitle(title);
        confirmationAlert.setContentText(message);


        result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get().

                equals(ok)) {
            return true;
        } else {
            confirmationAlert.close();
        }
        return false;
    }

    public static boolean singleConfirmationAlert(String message, String buttonText) {
        ButtonType payment = new ButtonType(buttonText);
        Optional<ButtonType> result;

        Alert singleConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, message, payment);
        singleConfirmationAlert.setTitle("Ma hubtaa");
        singleConfirmationAlert.setContentText(message);

        result = singleConfirmationAlert.showAndWait();
        return result.isPresent() && result.get().
                equals(payment);
    }
}

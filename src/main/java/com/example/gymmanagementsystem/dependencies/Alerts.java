package com.example.gymmanagementsystem.dependencies;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    public static void notificationAlert(String message) {
        Alert notificationAlert = new Alert(Alert.AlertType.INFORMATION);
        notificationAlert.setTitle("Habalyo");
        notificationAlert.setContentText(message);
        notificationAlert.showAndWait();

    }

    public static void waningAlert(String message) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Ma ogtahay?");
        warningAlert.setContentText(message);
        warningAlert.show();
    }

    public static void errorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Khalad ayaa dhacay!");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }


    public static boolean confirmationAlert(String message, String okBtnTitle, String cancelBtnTitle) {
        ButtonType ok = new ButtonType(okBtnTitle);
        ButtonType no = new ButtonType(cancelBtnTitle);
        Optional<ButtonType> result;

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, message, no, ok);
        confirmationAlert.setTitle("Ma hubtaa?");
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
        singleConfirmationAlert.setTitle("Ma hubtaa?");
        singleConfirmationAlert.setContentText(message);

        result = singleConfirmationAlert.showAndWait();
        return result.isPresent() && result.get().
                equals(payment);
    }
}

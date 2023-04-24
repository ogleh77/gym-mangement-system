package com.example.gymmanagementsystem.controllers.info;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WarningController extends CommonClass implements Initializable {
    @FXML
    private VBox vbox;
    private Stage thisStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> this.thisStage = (Stage) vbox.getScene().getWindow());
    }

    @FXML
    void cancelHandler() {
        closeStage(thisStage, vbox.getParent());
    }

    public void setOutdatedCustomers(ObservableList<Customers> outdatedCustomers) {
        if (!outdatedCustomers.isEmpty()) {
            FXMLLoader loader;
            AnchorPane anchorPane;
            for (Customers customer : outdatedCustomers) {
                loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/info/customer-card.fxml"));
                try {
                    anchorPane = loader.load();
                    anchorPane.setStyle("-fx-background-color: white;-fx-background-radius: 2");
                    CardController controller = loader.getController();
                    controller.setCustomer(customer);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                vbox.getChildren().add(anchorPane);
            }
        } else {
            Label label = new Label("Ma jirto macamiil wakhtigoodu dhamaad ku dhowyahay.");
            label.setStyle("-fx-font-size: 15;-fx-font-family: Verdana");
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(label);
            vbox.setPrefHeight(300);

        }

    }

}

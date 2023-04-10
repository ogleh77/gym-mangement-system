package com.example.gymmanagementsystem.controllers.services;

import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerProfileController extends CommonClass implements Initializable {
    @FXML
    private Label fulname;

    @FXML
    private Label gender;

    @FXML
    private ImageView imgView;

    @FXML
    private Label phone;

    @FXML
    private Label shift;

    @FXML
    private Label whoAdded;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.stage = (Stage) whoAdded.getScene().getWindow());
    }

    @FXML
    void closeStage() {
        closeStage(stage, whoAdded.getParent());
    }


    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        fulname.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        phone.setText(customer.getPhone());
        whoAdded.setText(customer.getWhoAdded());
        shift.setText(customer.getShift());
        if (customer.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
            Image image = new Image(bis);
            imgView.setImage(image);
        }
        if (customer.getGander().equals("Male")) {
            gender.setText("Male");
        } else {
            gender.setText("Female");
        }
    }


}

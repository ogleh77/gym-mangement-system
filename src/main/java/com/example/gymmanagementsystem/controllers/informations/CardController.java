package com.example.gymmanagementsystem.controllers.informations;


import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class CardController extends CommonClass implements Initializable {
    @FXML
    public Label shift;
    @FXML
    private ImageView customerPhoto;
    @FXML
    private Label fullName;
    @FXML
    private Label expDate;
    @FXML
    private Label outDated;
    @FXML
    private Label phone;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        LocalDate exp = customer.getPayments().get(0).getExpDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(exp, today);

        fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        expDate.setText(customer.getPayments().get(0).getExpDate() + "");

        if (period.getYears() > 0) {
            outDated.setText(period.getYears() + " sano ka hor.");
        } else if (period.getMonths() > 0) {
            outDated.setText(period.getMonths() == 1 ? period.getMonths() + " bil ka hor." : period.getMonths() + " bilood ka hor.");
        } else if (period.getDays() == 0) {
            outDated.setText("manta u hadhay.");
        } else if (period.getDays() < 0) {
            outDated.setText(Math.abs(period.getDays()) + " malmood baa u hadhay.");
        } else {
            outDated.setText(period.getDays() == 1 ? period.getDays() + " malin ka hor." : period.getDays() + " malmood ka hor.");
        }

        phone.setText(customer.getPhone());
        shift.setText(customer.getShift());
        if (customer.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
            Image image = new Image(bis);
            imageUploaded = true;
            customerPhoto.setImage(image);
        }
    }


    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }


}

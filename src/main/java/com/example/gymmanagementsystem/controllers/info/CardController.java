package com.example.gymmanagementsystem.controllers.info;


import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.helpers.CommonClass;
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
    private Label lastPaid;
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
        lastPaid.setText(customer.getPayments().get(0).getStartDate()+"");


        if (period.getYears() > 0) {
            outDated.setText(period.getYears() == 1 ? period.getYears() + " year ago " : period.getYears() + " years ago");
        } else if (period.getMonths() > 0) {
            outDated.setText(period.getMonths() == 1 ? period.getMonths() + " month ago" : period.getMonths() + " months ago");
        } else if (period.getDays() == 0) {
            outDated.setText("today is left");
        } else if (period.getDays() < 0) {
            outDated.setText(Math.abs(period.getDays()) + " days left");
        } else {
            outDated.setText(period.getDays() == 1 ? period.getDays() + " day ago" : period.getDays() + " days ago");
        }

        System.out.println(customer.getFirstName() + " " + period);
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

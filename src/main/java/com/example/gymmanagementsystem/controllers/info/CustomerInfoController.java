package com.example.gymmanagementsystem.controllers.info;

import com.example.gymmanagementsystem.dao.main.PaymentService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.example.gymmanagementsystem.helpers.CustomException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerInfoController extends CommonClass implements Initializable {
    @FXML
    private ImageView imgView;
    @FXML
    private Label fullName;
    @FXML
    private Label address;
    @FXML
    private Label phone;
    @FXML
    private Label shift;
    @FXML
    private Label weight;
    @FXML
    private Label gander;
    @FXML
    private Label whoAdded;
    @FXML
    private Label chest;
    @FXML
    private Label hips;
    @FXML
    private Label foreArm;
    @FXML
    private Label waist;
    @FXML
    private TableColumn<Payments, String> amountPaid;

    @FXML
    private TableColumn<Payments, String> discount;

    @FXML
    private TableColumn<Payments, LocalDate> expDate;

    @FXML
    private TableColumn<Payments, String> month;

    @FXML
    private TableColumn<Payments, String> paidBy;

    @FXML
    private TableColumn<Payments, String> paymentDate;

    @FXML
    private TableColumn<Payments, String> pend;
    @FXML
    private TableColumn<Payments, String> poxing;
    @FXML
    private TableColumn<Payments, String> running;
    @FXML
    private TableColumn<Payments, String> daysRemind;
    @FXML
    private TableView<Payments> tableView;
    @FXML
    private TableColumn<Payments, String> vipBox;
    @FXML
    private TableColumn<Payments, String> year;
    private ObservableList<Payments> payments;
    private final Gym currentGym;

    private final String pendStyle;
    private final String unPendStyle;

    private ButtonType ok;

    private ButtonType cancel;

    public CustomerInfoController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.pendStyle = "-fx-background-color: #afd6e3;-fx-text-fill: black;-fx-font-family:Verdana;" + "-fx-pref-width: 100;-fx-font-size: 15";
        this.unPendStyle = "-fx-background-color: red;-fx-text-fill: white;-fx-font-family:Verdana;" + "-fx-pref-width: 100;-fx-font-size: 15";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::initFields);
    }

    @FXML
    void pendHandler() {
    }

    @FXML
    public void editHandler() {
    }

    @FXML
    public void deleteHandler() {
    }
    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        if (customer != null) {
            fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
            phone.setText(customer.getPhone());
            gander.setText(customer.getGander());
            address.setText(customer.getAddress() == null ? " no address " : customer.getAddress());
            shift.setText(customer.getShift());
            weight.setText(customer.getWeight() + "");
            whoAdded.setText(customer.getWhoAdded());

            try {
                payments = PaymentService.fetchAllPayments(customer.getPhone());
            } catch (CustomException e) {
                errorMessage(e.getMessage());
            }
            if (customer.getImage() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
                Image image = new Image(bis);
                imageUploaded = true;
                imgView.setImage(image);
            }
        }
    }



    //---------------------init fields-----------------–

    private void initFields() {
        if (payments.isEmpty()) {
            tableView.setPlaceholder(new Label("MACMIILKU PAYMENTS MALEH.."));
        } else {
            amountPaid.setCellValueFactory(payment -> new SimpleStringProperty("$" + payment.getValue().getAmountPaid()));
            discount.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getDiscount() == 0 ?
                    payment.getValue().getDiscount() + "" : "$" + payment.getValue().getDiscount()));
            expDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
            month.setCellValueFactory(new PropertyValueFactory<>("month"));
            paidBy.setCellValueFactory(new PropertyValueFactory<>("paidBy"));
            paymentDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            poxing.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isPoxing() ? "√" : "X"));
            running.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isOnline() ? "√" : "X"));
            pend.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isPending() ? "√" : "X"));

            daysRemind.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getDaysRemind()));

            vipBox.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getBox() != null ? "√" : "X"));
            year.setCellValueFactory(new PropertyValueFactory<>("year"));
            tableView.setItems(payments);
        }


    }


    private void pendPayment(Payments payment) throws SQLException {
        LocalDate exp = payment.getExpDate();
        LocalDate pendingDate = LocalDate.now();

        System.out.println("Exp date:- " + exp);
        System.out.println("pend date:- " + pendingDate);
        int daysRemind = Period.between(pendingDate, exp).getDays();
        int month = Period.between(pendingDate, exp).getMonths();
        System.out.println(month);
        if (month > 0) {
            daysRemind = 30;
        }
        System.out.println("Days rem " + daysRemind);
        if (ok == null && cancel == null) {
            ok = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
            cancel = new ButtonType("Maya!", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad hakisto paymentkan oo ay u hadhay \n" + "Wakhtigiisa dhicitaanka " + daysRemind + " malmood", ok, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            PaymentService.holdPayment(payment, currentGym.getPendingDate());
            payment.setPending(true);
            payment.setOnline(false);
            tableView.refresh();
        } else {
            alert.close();
        }
    }

    private void unPayment(Payments payment) throws SQLException {

        if (ok == null && cancel == null) {
            ok = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
            cancel = new ButtonType("Maya!", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad dib u furto paymentkan ", ok, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            PaymentService.unHoldPayment(payment);
            payment.setPending(false);
            payment.setOnline(true);
            payment.setExpDate(payment.getExpDate());
        } else {
            alert.close();
        }
    }


}

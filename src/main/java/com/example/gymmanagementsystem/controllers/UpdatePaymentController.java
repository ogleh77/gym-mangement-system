package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.data.dto.BoxService;
import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.dto.main.PaymentService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.entities.service.Box;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdatePaymentController extends CommonClass implements Initializable {
    @FXML
    private TextField amountPaid;

    @FXML
    private ComboBox<Box> boxChooser;

    @FXML
    private JFXButton createBtn;

    @FXML
    private TextField discount;

    @FXML
    private Label discountValidation;

    @FXML
    private DatePicker expDate;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstName;

    @FXML
    private ImageView imgView;
    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;

    @FXML
    private TextField middleName;

    @FXML
    private ComboBox<String> paidBy;

    @FXML
    private TextField phone;

    @FXML
    private JFXCheckBox poxing;

    @FXML
    private DatePicker startDate;
    @FXML
    private Label dateInfo;
    private Payments payment;
    private double _amountPaid;
    private double _discount;
    private final Gym currentGym;

    public UpdatePaymentController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            getMandatoryFields().addAll(amountPaid, paidBy);
            paidBy.setItems(getPaidBy());
            expDate.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 1);
                }
            });
        });
        amountValidation();
        validateDiscount();
        service.setOnSucceeded(e -> createBtn.setText("Bedeshay(updated)"));
    }


    @FXML
    void createPaymentHandler() {
        try {
            _discount = (!discount.getText().isEmpty() || !discount.getText().isBlank() ? Double.parseDouble(discount.getText()) : 0);
            _amountPaid = (!amountPaid.getText().isEmpty() || !amountPaid.getText().isBlank() ? Double.parseDouble(amountPaid.getText()) : 0);
            if (isValid(getMandatoryFields(), null) && (!discountValidation.isVisible() || discount.getText().length() == 0)) {
                startTask(service, createBtn, "Updating");
            }
        } catch (Exception e) {
            if (e.getMessage().matches("multiple points"))
                Alerts.waningAlert("Fadlan hubi inaad si sax ah u gelisay discount ka ama amount ka la bixshay" + " Kana masax hadii points badan ku jiraan error caused by " + e.getMessage());
            else {
                Alerts.errorAlert(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void resetHandler() {
        discount.setText(String.valueOf(0));
        amountPaid.setText("");
        boxChooser.setValue(null);
        paidBy.setValue(null);
    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        if (customer != null) {
            firstName.setText(customer.getFirstName());
            middleName.setText(customer.getFirstName());
            lastName.setText(customer.getFirstName());
            middleName.setText(customer.getMiddleName());
            lastName.setText(customer.getLastName());
            phone.setText(customer.getPhone());
            male.setSelected(customer.getGander().equals("Male"));
            female.setSelected(customer.getGander().equals("Female"));

            if (customer.getImage() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
                Image image = new Image(bis);
                imageUploaded = true;
                imgView.setImage(image);
            }
        }
    }

    public void setPayment(Payments payment) {
        this.payment = payment;
        expDate.setValue(payment.getExpDate());
        startDate.setValue(payment.getStartDate());
        amountPaid.setText(String.valueOf(payment.getAmountPaid()));
        poxing.setSelected(payment.isPoxing());
        discount.setText(String.valueOf(payment.getDiscount()));
        paidBy.setValue(payment.getPaidBy());

        createBtn.setText("Wax ka bedel");
        if (!payment.isOnline()) {
            startDate.setStyle("-fx-opacity: 1");
            startDate.setDisable(true);
            expDate.setDisable(true);
            expDate.setStyle("-fx-opacity: 1");
            dateInfo.setVisible(true);
            boxChooser.setValue(payment.getBox() != null ? payment.getBox() : new Box(0, "Khanad maleh", false));
         } else {
            boxChooser.getItems().add(new Box(0, "remove box", false));
            if (payment.getBox() == null) {
                currentGym.getVipBoxes().stream().filter(Box::isReady).forEach(box -> boxChooser.getItems().add(box));
            } else {
                boxChooser.setValue(payment.getBox());
            }
        }
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        updatePayment();
                        Thread.sleep(1000);
                        Platform.runLater(() -> {
                            boolean data = Alerts.singleConfirmationAlert("Wax ka bedelka payment-ka wad ku guulaystay.", "Back to home");
                            if (data) {
                                System.out.println("Home");
                            }
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };


    //---------------------Helper methods---------------------–
    private void validateDiscount() {
        discount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                discount.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
            if (!discount.getText().isBlank()) {

                double _discount = Double.parseDouble(discount.getText());
                if (_discount > currentGym.getMaxDiscount()) {
                    discountValidation.setText("Qimo dhimista u badani waa $" + currentGym.getMaxDiscount());
                    discountValidation.setVisible(true);
                } else {
                    discountValidation.setVisible(false);
                }
            }
        });

    }

    private void amountValidation() {
        amountPaid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountPaid.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void updatePayment() throws SQLException {
        boxInit();
        payment.setPaymentID(payment.getPaymentID());
        payment.setStartDate(startDate.getValue());
        payment.setExpDate(expDate.getValue());
        payment.setDiscount(_discount);
        payment.setAmountPaid(_amountPaid);
        payment.setPoxing(poxing.isSelected());
        payment.setOnline(payment.isOnline());
        payment.setPending(payment.isPending());
        payment.setPaidBy(paidBy.getValue());
        expDate.setValue(expDate.getValue());
        startDate.setValue(startDate.getValue());
        PaymentService.updatePayment(payment);
    }

    private void boxInit() throws SQLException {
        Box alreadyBox = payment.getBox();

        if (alreadyBox != null) {
            if (boxChooser.getValue().getBoxName().equals("remove box")) {
                payment.setBox(null);
                BoxService.changeBoxState(alreadyBox);
            }
        }

        if (alreadyBox == null) {
            if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().equals("remove box")) {
                payment.setBox(boxChooser.getValue());
                BoxService.changeBoxState(payment.getBox());
            }

        }
    }
}
package com.example.gymmanagementsystem.controllers.main.payments;

import animatefx.animation.FadeIn;
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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreatePaymentController extends CommonClass implements Initializable {

    @FXML
    private TextField amountPaid;

    @FXML
    private ComboBox<Box> boxChooser;

    @FXML
    private JFXButton createBtn;

    @FXML
    private Label dateLabel;

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
    private Label infoMin;

    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;

    @FXML
    private TextField middleName;

    @FXML
    private ComboBox<String> paidBy;

    @FXML
    private Label paymentInfo;

    @FXML
    private TextField phone;

    @FXML
    private JFXCheckBox poxing;

    @FXML
    private DatePicker startDate;
    private final Gym currentGym;
    private Payments payment;
    private ObservableList<Payments> paymentsList;
    private double _amountPaid;
    private double _discount;

    public CreatePaymentController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            expDate.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 1);
                }
            });

            if (payment == null) {
                init();
            }
        });
        amountValidation();
        validateDiscount();
    }


    @FXML
    void createPaymentHandler() {
        try {
            _discount = (!discount.getText().isEmpty() || !discount.getText().isBlank() ? Double.parseDouble(discount.getText()) : 0);
            _amountPaid = (!amountPaid.getText().isEmpty() || !amountPaid.getText().isBlank() ? Double.parseDouble(amountPaid.getText()) : 0);
            if (isValid(getMandatoryFields(), null) && (!discountValidation.isVisible() || discount.getText().length() == 0)) {
                insetPayment();
//                startTask(service, createBtn, newPayment ? "Creating" : "Updating");
            }
        } catch (Exception e) {
            if (e.getMessage().matches("multiple points"))
                Alerts.waningAlert("Fadlan hubi inaad si sax ah u gelisay discount ka ama amount ka la bixshay" + " Kana masax hadii points badan ku jiraan error caused by " + e.getMessage());
            else Alerts.errorAlert(e.getMessage());
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

    public void checkPayment(Customers customer) {
        try {
            paymentsList = PaymentService.fetchAllPayments(customer.getPhone());
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
        for (Payments payment : paymentsList) {
            if (payment.isOnline()) {
                this.payment = payment;
                blockFields(payment);
                break;
            } else if (payment.isPending()) {
                blockFields(payment);
                this.payment = payment;
                break;
            }
        }
    }

    //----------------------------Helper methods---------------------
    private void insetPayment() throws SQLException {
        Payments payment = new Payments(0, LocalDate.now(), expDate.getValue(), String.valueOf(LocalDate.now().getMonth()), String.valueOf(LocalDate.now().getYear()), _amountPaid, paidBy.getValue(), _discount, poxing.isSelected(), customer.getPhone(), true, false);
        if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
            payment.setBox(boxChooser.getValue());
        }
        customer.getPayments().add(0, payment);
        createBtn.setDisable(true);
        PaymentService.insertPayment(customer);
    }

    private void validateDiscount() throws NumberFormatException {
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

    private void init() {
        currentGym.getVipBoxes().stream().filter(Box::isReady).forEach(box -> boxChooser.getItems().add(box));
        boxChooser.getItems().add(new Box(0, "remove box", false));
        getMandatoryFields().addAll(amountPaid, paidBy);
        startDate.setValue(LocalDate.now());
        expDate.setValue(LocalDate.now().plusDays(30));
        paidBy.setItems(super.getPaidBy());
    }

    private void amountValidation() {
        amountPaid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountPaid.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void tellInfo(LocalDate expDate, boolean isPending) {
        paymentInfo.setText(isPending ? "Macmillka payment ayaa u xidhan" : "Macmiilkan wakhtigu kama dhicin ");
        infoMin.setText(isPending ? "Macmiilka waxa u xidhay payment saaso ay tahay looma samayn karo " + "payment cusub" : "wuxuse ka dhaacyaa [" + expDate.toString() + "] Insha Allah");
        paymentInfo.setStyle("-fx-text-fill: #d20e0e; -fx-font-family: Verdana;-fx-font-size: 15");
        FadeIn fadeIn = new FadeIn(paymentInfo);
        fadeIn.setCycleCount(20);
        fadeIn.setDelay(Duration.millis(100));
        fadeIn.play();
    }

    private void blockFields(Payments payment) {
        amountPaid.setEditable(false);
        amountPaid.setText(String.valueOf(payment.getAmountPaid()));

        poxing.setSelected(payment.isPoxing());
        poxing.setDisable(true);
        poxing.setStyle("-fx-opacity: 1");

        paidBy.setValue(payment.getPaidBy());
        paidBy.setEditable(false);

        boxChooser.setValue(payment.getBox() != null ? payment.getBox() : new Box(0, "Khanad maleh", false));

        discount.setText(payment.getDiscount() + "");
        discount.setEditable(false);

        startDate.setStyle("-fx-opacity: 1");
        startDate.setDisable(true);
        startDate.setValue(payment.getStartDate());

        expDate.setDisable(true);
        expDate.setStyle("-fx-opacity: 1");
        expDate.setValue(payment.getStartDate());

        createBtn.setDisable(true);

        tellInfo(payment.getExpDate(), payment.isPending());
    }
}

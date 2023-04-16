package com.example.gymmanagementsystem.controllers.main;

import animatefx.animation.FadeIn;
import com.example.gymmanagementsystem.dao.main.PaymentService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Box;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController extends CommonClass implements Initializable {
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
    @FXML
    private Label topLabel;
    private ObservableList<Payments> paymentsList;
    private final Gym currentGym;
    private Payments payment;
    private Payments updatePayment;
    private double _amountPaid;
    private double _discount;
    private boolean newPayment = true;

    public PaymentController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initFields();
            if (updatePayment == null) {
                startDate.setValue(LocalDate.now());
                expDate.setValue(LocalDate.now().plusDays(30));

            }
        });
        try {
            amountValidation();
            validateDiscount();
        } catch (Exception e) {
            System.out.println("Not " + e.getMessage());
        }


        service.setOnSucceeded(e -> {
            createBtn.setText(newPayment ? "Created" : "Updated");
            createBtn.setGraphic(null);
        });
    }

    @FXML
    void createPaymentHandler() {
        try {

            _discount = (!discount.getText().isEmpty() || !discount.getText().isBlank() ? Double.parseDouble(discount.getText()) : 0);
            _amountPaid = (!amountPaid.getText().isEmpty() || !amountPaid.getText().isBlank() ? Double.parseDouble(amountPaid.getText()) : 0);
            checkDate(startDate.getValue(), expDate.getValue());

            if (isValid(getMandatoryFields(), null)) {
                startTask(service, createBtn, newPayment ? "Creating" : "Updating");
            }
        } catch (Exception e) {
            errorMessage("Fadlan si sax ah u geli discount ka ama amount ka la bixshay" + e.getMessage());
        }
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


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        if (newPayment) {
                            insetPayment();
                            Thread.sleep(1000);
                            Platform.runLater(() -> {
                                Optional<ButtonType> result = informationAlert("New payment Created Successfully").showAndWait();
                                if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                                    System.out.println("Back to home");
                                }
                            });
                        } else {
                            updatePayment();
                            Thread.sleep(1000);
                            Platform.runLater(() -> {
                                Optional<ButtonType> result = informationAlert("Payment updated Successfully").showAndWait();
                                if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                                    System.out.println("Ok");
                                }
                            });
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                    }
                    return null;
                }
            };
        }

    };


    //--------------------------_Helpers_-------------------

    public void checkPayment(Customers customer) {
        try {
            paymentsList = PaymentService.fetchAllPayments(customer.getPhone());
        } catch (SQLException e) {
            errorMessage(e.getMessage());
        }
        for (Payments payment : paymentsList) {
            if (payment.isOnline()) {
                this.payment = payment;
                blockFields(payment);
                newPayment = false;
                break;

            } else if (payment.isPending()) {
                this.payment = payment;
                blockFields(payment);
                newPayment = false;
                break;
            }
        }
    }

    public void setUpdatePayment(Payments updatePayment) {
        this.updatePayment = updatePayment;
        amountPaid.setText(String.valueOf(updatePayment.getAmountPaid()));
        poxing.setSelected(updatePayment.isPoxing());
        discount.setText(String.valueOf(updatePayment.getDiscount()));
        paidBy.setValue(updatePayment.getPaidBy());
        if (updatePayment.getBox() != null) {
            boxChooser.setValue(updatePayment.getBox());
        }
        createBtn.setText("Update payment");
        topLabel.setText("UPDATE PAYMENT FORM");
        if (LocalDate.now().isAfter(updatePayment.getExpDate()) ||
                LocalDate.now().isEqual(updatePayment.getExpDate())) {
            startDate.setDisable(true);
            expDate.setDisable(true);
        }
        startDate.setValue(updatePayment.getStartDate());
        expDate.setValue(updatePayment.getExpDate());

        newPayment = false;
    }

    private void initFields() {
        getMandatoryFields().addAll(amountPaid, paidBy);
        paidBy.setItems(getPaidBy());
        if (boxChooser.getItems().isEmpty()) {
            for (Box box : currentGym.getVipBoxes()) {
                if (box.isReady()) boxChooser.getItems().add(box);
            }
        }
        boxChooser.getItems().add(new Box(0, "remove box", false));
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

    private void amountValidation() {
        amountPaid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountPaid.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void blockFields(Payments payment) {
        amountPaid.setEditable(false);
        amountPaid.setText(String.valueOf(payment.getAmountPaid()));
        poxing.setSelected(payment.isPoxing());
        poxing.setDisable(true);
        poxing.setStyle("-fx-opacity: 1");
        paidBy.setValue(payment.getPaidBy());
        paidBy.setEditable(false);
        boxChooser.setValue(payment.getBox() != null ? payment.getBox() : null);
        boxChooser.setEditable(false);
        discount.setText(payment.getDiscount() + "");
        discount.setEditable(false);
        expDate.setValue(payment.getExpDate());
        createBtn.setDisable(true);
        startDate.setValue(payment.getStartDate());
        startDate.setStyle("-fx-opacity: 1");
        startDate.setDisable(true);
        expDate.setDisable(true);
        expDate.setStyle("-fx-opacity: 1");
        tellInfo(payment.getExpDate(), payment.isPending());
    }

    private void tellInfo(LocalDate expDate, boolean isPending) {
        paymentInfo.setText(isPending ? "Macmillka payment ayaa u xidhan" : "Macmiilkan wakhtigu kama dhicin ");
        infoMin.setText(isPending ? "Macmiilka waxa u xidhay payment saaso ay tahay looma samayn karo " + "payment cusub" : "wuxuse ka dhaacyaa [" + expDate.toString() + "] Insha Allah");
        paymentInfo.setStyle("-fx-text-fill: #d20e0e; -fx-font-family: Verdana;-fx-font-size: 15");
        FadeIn fadeIn = new FadeIn(paymentInfo);
        fadeIn.setCycleCount(50);
        fadeIn.setDelay(Duration.millis(100));
        fadeIn.play();
    }

    private void updatePayment() throws SQLException {
        if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
            updatePayment.setBox(boxChooser.getValue());
        } else {
            updatePayment.setBox(null);
        }
        updatePayment.setPaymentID(updatePayment.getPaymentID());
        updatePayment.setStartDate(startDate.getValue());
        updatePayment.setExpDate(expDate.getValue());
        updatePayment.setDiscount(_discount);
        updatePayment.setAmountPaid(_amountPaid);
        updatePayment.setPoxing(poxing.isSelected());
        updatePayment.setOnline(updatePayment.isOnline());
        updatePayment.setPending(updatePayment.isPending());
        updatePayment.setPaidBy(paidBy.getValue());
        PaymentService.updatePayment(updatePayment);
    }

    private void insetPayment() throws SQLException {
        Payments payment = new Payments(0, LocalDate.now(), expDate.getValue(), String.valueOf(LocalDate.now().getMonth()), String.valueOf(LocalDate.now().getYear()), _amountPaid, paidBy.getValue(), _discount, poxing.isSelected(), customer.getPhone(), true, false);

        if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
            payment.setBox(boxChooser.getValue());
        }
        customer.getPayments().add(0, payment);
        PaymentService.insertPayment(customer);
    }

    private void checkDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Wakhtiga bilowgu kama danbayn karo wakhtiga dhamanayo paymentku");
        }
    }
}

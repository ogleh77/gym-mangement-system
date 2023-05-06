package com.example.gymmanagementsystem.controllers.notdone;

import animatefx.animation.FadeIn;
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
import java.util.ResourceBundle;

public class PaymentsControllers extends CommonClass implements Initializable {
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
    private final Gym currentGym;
    private double _amountPaid;
    private double _discount;
    private boolean newPayment = true;
    private Payments paymentUpdating;
    private ObservableList<Payments> paymentsList;


    public PaymentsControllers() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            getMandatoryFields().addAll(amountPaid, paidBy);
            paidBy.setItems(getPaidBy());
            if (paymentUpdating == null) {
                insertInitFields();
            }
            amountValidation();
            validateDiscount();
            expDate.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 1);
                }
            });
        });

        service.setOnSucceeded(e -> {
            createBtn.setText(newPayment ? "Created" : "Updated");
            createBtn.setGraphic(null);
        });
    }

    @FXML
    void createPaymentHandler() throws SQLException {
        try {
            _discount = (!discount.getText().isEmpty() || !discount.getText().isBlank() ? Double.parseDouble(discount.getText()) : 0);
            _amountPaid = (!amountPaid.getText().isEmpty() || !amountPaid.getText().isBlank() ? Double.parseDouble(amountPaid.getText()) : 0);
            if (isValid(getMandatoryFields(), null) && (!discountValidation.isVisible() || discount.getText().length() == 0)) {
                startTask(service, createBtn, newPayment ? "Creating" : "Updating");
            }
        } catch (Exception e) {
            if (e.getMessage().matches("multiple points"))
                Alerts.waningAlert("Fadlan hubi inaad si sax ah u gelisay discount ka ama amount ka la bixshay" + " Kana masax hadii points badan ku jiraan error caused by " + e.getMessage());
            else Alerts.errorAlert(e.getMessage());
        }
        updatePayment();
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

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {

                        Thread.sleep(1000);
                        if (newPayment) {
                            insetPayment();
                            Platform.runLater(() -> {
                                boolean data = Alerts.singleConfirmationAlert("New payment Created Successfully", "Back to home");
                                if (data) {
//                                    openHome();
                                    System.out.println("good");
                                }
                            });
                        } else {
                            updatePayment();
                            Platform.runLater(() -> {
                                boolean data = Alerts.singleConfirmationAlert("Payment updated Successfully", "Back to home");
                                if (data) {
                                    System.out.println("Home");
                                }
                            });
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }

    };

    public void checkPayment(Customers customer) {
        try {
            paymentsList = PaymentService.fetchAllPayments(customer.getPhone());
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
        for (Payments payment : paymentsList) {
            if (payment.isOnline()) {
                blockFields(payment);
                newPayment = false;
                break;
            } else if (payment.isPending()) {
                blockFields(payment);
                newPayment = false;
                break;
            }
        }
    }

    //----------------------Helper methods-----------------------–
    private void insetPayment() throws SQLException {
        Payments payment = new Payments(0, LocalDate.now(), expDate.getValue(), String.valueOf(LocalDate.now().getMonth()), String.valueOf(LocalDate.now().getYear()), _amountPaid, paidBy.getValue(), _discount, poxing.isSelected(), customer.getPhone(), true, false);
        if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
            payment.setBox(boxChooser.getValue());
        }
        customer.getPayments().add(0, payment);
        createBtn.setDisable(true);
        PaymentService.insertPayment(customer);
    }

    private void updatePayment() throws SQLException {

        boxInit();

        paymentUpdating.setPaymentID(paymentUpdating.getPaymentID());
        paymentUpdating.setStartDate(startDate.getValue());
        paymentUpdating.setExpDate(expDate.getValue());
        paymentUpdating.setDiscount(_discount);
        paymentUpdating.setAmountPaid(_amountPaid);
        paymentUpdating.setPoxing(poxing.isSelected());
        paymentUpdating.setOnline(paymentUpdating.isOnline());
        paymentUpdating.setPending(paymentUpdating.isPending());
        paymentUpdating.setPaidBy(paidBy.getValue());
        PaymentService.updatePayment(paymentUpdating);
    }

    private void boxInit() throws SQLException {
        Box alreadyBox = paymentUpdating.getBox();

        if (alreadyBox != null) {
            if (boxChooser.getValue().getBoxName().matches("remove box")) {
                System.out.println("Box exist already but removed");
                paymentUpdating.setBox(null);
                BoxService.changeBoxState(alreadyBox);
            } else {
                System.out.println("re asinged its Box exist already ");
                paymentUpdating.setBox(boxChooser.getValue());
                BoxService.changeBoxState(boxChooser.getValue());
            }
        } else {
            if (boxChooser.getValue() != null && !boxChooser.getValue().getBoxName().matches("remove box")) {
                paymentUpdating.setBox(boxChooser.getValue());
                BoxService.changeBoxState(boxChooser.getValue());
            } else {
                paymentUpdating.setBox(null);
            }
        }

    }

    public void setPaymentUpdating(Payments paymentUpdating) {
        this.paymentUpdating = paymentUpdating;
        initUpdatingFields();
        closeDatePickersIfOutDated();

        if (paymentUpdating.getBox() == null) {
            currentGym.getVipBoxes().stream().filter(Box::isReady).forEach(box -> boxChooser.getItems().add(box));
            boxChooser.getItems().add(new Box(0, "remove box", false));
        } else {
            boxChooser.setValue(paymentUpdating.getBox());
            boxChooser.getItems().add(new Box(0, "remove box", false));
        }
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


    //----------------------------payment inserted----------------------------
    private void insertInitFields() {
        currentGym.getVipBoxes().stream().filter(Box::isReady).forEach(box -> boxChooser.getItems().add(box));
        boxChooser.getItems().add(new Box(0, "remove box", false));

    }

    //----------------------------payment update----------------------------

    private void closeDatePickersIfOutDated() {
        if (!paymentUpdating.isOnline()) {
            startDate.setDisable(true);
            expDate.setDisable(true);
        }
        startDate.setValue(paymentUpdating.getStartDate());
        expDate.setValue(paymentUpdating.getExpDate());

        newPayment = false;
    }

    private void initUpdatingFields() {
        amountPaid.setText(String.valueOf(paymentUpdating.getAmountPaid()));
        poxing.setSelected(paymentUpdating.isPoxing());
        discount.setText(String.valueOf(paymentUpdating.getDiscount()));
        paidBy.setValue(paymentUpdating.getPaidBy());
        createBtn.setText("Update payment");
        topLabel.setText("FORM KA WAX KA BEDELKA PAYMENT-KA");
    }

    private void blockFields(Payments payment) {
        amountPaid.setEditable(false);
        amountPaid.setText(String.valueOf(payment.getAmountPaid()));

        poxing.setSelected(payment.isPoxing());
        poxing.setDisable(true);
        poxing.setStyle("-fx-opacity: 1");

        paidBy.setValue(payment.getPaidBy());
        paidBy.setEditable(false);

        boxChooser.setValue(payment.getBox() != null ? payment.getBox() :
                new Box(0, "Khanad maleh", false));

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

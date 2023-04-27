package com.example.gymmanagementsystem.controllers.info;

import com.example.gymmanagementsystem.controllers.main.PaymentController;
import com.example.gymmanagementsystem.dao.main.PaymentService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

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
    @FXML
    private JFXButton pendBtn;
    private ObservableList<Payments> payments;
    private final Gym currentGym;
    private final String pendStyle;
    private final String unPendStyle;
    private ButtonType ok;
    private ButtonType cancel;

    public CustomerInfoController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.pendStyle = "-fx-background-color: #328ca8;-fx-text-fill: white;-fx-font-family:Verdana;" + "-fx-font-size: 14";
        this.unPendStyle = "-fx-background-color: red;-fx-text-fill: black;-fx-font-family:Verdana;" + "-fx-font-size: 14";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initFields);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                if (!newValue.isPending() && !newValue.isOnline()) {
                    pendBtn.setDisable(true);
                    pendBtn.setText("Haki(pend)");
                    pendBtn.setStyle(pendStyle);
                } else if (newValue.isPending()) {
                    pendBtn.setText("Fur");
                    pendBtn.setStyle(unPendStyle);
                    pendBtn.setDisable(false);
                } else {
                    pendBtn.setText("Haki(pend)");
                    pendBtn.setStyle(pendStyle);
                    pendBtn.setDisable(false);
                }
        });
    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        if (customer != null) {
            fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
            phone.setText(customer.getPhone());
            gander.setText(customer.getGander());
            address.setText(customer.getAddress() == null ? "no address " : customer.getAddress());
            shift.setText(customer.getShift());
            weight.setText(customer.getWeight() + " kg");
            whoAdded.setText(customer.getWhoAdded());
            waist.setText(customer.getWaist() + " cm");
            hips.setText(customer.getHips() + " cm");
            foreArm.setText(customer.getForeArm() + " cm");
            chest.setText(customer.getChest() + " cm");

            payments = customer.getPayments();

            if (customer.getImage() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
                Image image = new Image(bis);
                imageUploaded = true;
                imgView.setImage(image);
            }
        }
    }

    @FXML
    void pendHandler() {
        Payments selectedPayment = tableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedPayment == null) {
                throw new RuntimeException("No payment selected");
            }
            checkPayment(selectedPayment);
        } catch (Exception e) {
            infoAlert(e.getMessage());
        }
    }

    @FXML
    void editHandler() {
        Payments selectedPayment = tableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedPayment == null) {
                throw new RuntimeException("No payment selected");
            }
            FXMLLoader loader = openNormalWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml", borderPane);
            PaymentController controller = loader.getController();
            controller.setUpdatePayment(selectedPayment);
            controller.setCustomer(customer);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void deleteHandler() {

        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Payments payment = tableView.getSelectionModel().getSelectedItem();
            ButtonType haa = new ButtonType("Haa", ButtonBar.ButtonData.YES);
            ButtonType maya = new ButtonType("Maya", ButtonBar.ButtonData.NO);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inaad masaxdo paymentkan", haa, maya);
            alert.setTitle("Digniin");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(haa)) {
                try {
                    PaymentService.deletePayment(payment);
                    payments.remove(payment);
                } catch (Exception e) {
                    errorMessage(e.getMessage());
                }
            } else {
                alert.close();
            }
        }

    }

    //----------------------Helper methods----------------
    private void checkPayment(Payments payment) throws SQLException {
        if (payment.isPending()) {
            unPayment(payment);
        } else {
            pendPayment(payment);
        }
        tableView.refresh();
    }

    private void initFields() {
        if (payments.isEmpty()) {
            tableView.setPlaceholder(new Label("MACMIILKU PAYMENTS MALEH.."));
        } else {
            amountPaid.setCellValueFactory(payment -> new SimpleStringProperty("$" + payment.getValue().getAmountPaid()));
            discount.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getDiscount() == 0 ? payment.getValue().getDiscount() + "" : "$" + payment.getValue().getDiscount()));
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
        String daysRemain = getDaysRemind(exp);
        if (ok == null && cancel == null) {
            ok = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
            cancel = new ButtonType("Maya!", ButtonBar.ButtonData.CANCEL_CLOSE);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad hakisto paymentkan oo ay u hadhay \n" + "Wakhtigiisa dhicitaanka " + daysRemain, ok, cancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            PaymentService.holdPayment(payment, currentGym.getPendingDate());
            payment.setPending(true);
            payment.setOnline(false);
            pendBtn.setText("Fur");
            pendBtn.setStyle(unPendStyle);
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
            pendBtn.setText("Haki");
            pendBtn.setStyle(pendStyle);
        } else {
            alert.close();
        }
    }

    private String getDaysRemind(LocalDate expDate) {
        Period period = Period.between(LocalDate.now(), expDate);
        if (period.getYears() > 0) {
            return period.getYears() + " sano " + (period.getMonths() > 0 ? "& " + period.getMonths() + " bilood" : "")
                    + (period.getDays() > 0 ? " & " + period.getDays() + "malmood" : "");
        } else if (period.getMonths() > 0) {
            return period.getMonths() + " bilood " + (period.getDays() > 0 ? " & " + period.getDays() + "malmood" : "");
        } else if (period.getDays() > 0) {
            return period.getDays() == 1 ? "1 maalin" : period.getDays() + " malmood";
        }
        return "outdated";
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }
}
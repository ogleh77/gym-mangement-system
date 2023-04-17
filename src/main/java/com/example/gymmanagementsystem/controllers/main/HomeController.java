package com.example.gymmanagementsystem.controllers.main;

import com.example.gymmanagementsystem.controllers.info.CustomerInfoController;
import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController extends CommonClass implements Initializable {
    @FXML
    private TableColumn<Customers, Integer> customerId;

    @FXML
    private TableColumn<Customers, String> fullName;

    @FXML
    private TableColumn<Customers, String> gander;
    @FXML
    private TableColumn<Customers, String> phone;

    @FXML
    private TableColumn<Customers, String> shift;
    @FXML
    private TableView<Customers> tableView;

    @FXML
    private TableColumn<Customers, String> address;

    @FXML
    private TableColumn<Customers, String> imagePath;
    @FXML
    private TableColumn<Customers, String> status;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<Customers, String> weight;
    @FXML
    private TableColumn<Customers, String> waist;
    private ObservableList<Customers> customersList;
    private FilteredList<Customers> filteredList;
    @FXML
    private Label usersCount;
    @FXML
    private Label customersCount;
    @FXML
    private Label zaad;
    @FXML
    private Label edahab;
    private final int nextUserId;
    private final int nextCustomerId;

    private final Gym currentGym;

    public HomeController() throws SQLException {
        nextUserId = (UserService.predictNextId() - 1);
        nextCustomerId = (CustomerService.predictNextId() - 1);
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initTable();
            searchFilter();
            usersCount.setText(nextUserId == 1 ? nextUserId + " user" : nextUserId + " users");
            customersCount.setText(nextCustomerId == 1 ? nextCustomerId + " member" : nextCustomerId + " members");
            edahab.setText("eDahab: " + currentGym.geteDahab());
            zaad.setText("Zaad: " + currentGym.getZaad());
        });

        //        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
//            @Override
//            public void changed(ObservableValue<? extends Customers> observable, Customers oldValue, Customers newValue) {
//
//
//             if (tableView.getSelectionModel().set)
//
//                FXMLLoader loader = new FXMLLoader(getClass().
//                        getResource("/com/example/gymmanagementsystem/views/service/short-info.fxml"));
//                try {
//                    Scene scene = new Scene(loader.load());
//                    CustomerProfileController controller = loader.getController();
//                    controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
//                    Stage stage = new Stage(StageStyle.UNDECORATED);
//                    stage.setScene(scene);
//                    stage.initModality(Modality.APPLICATION_MODAL);
//                    stage.show();
//                } catch (IOException e) {
//                    errorMessage(e.getMessage());
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
    }

    public void paymentHandler() {
        Customers customer = tableView.getSelectionModel().getSelectedItem();
        try {
            if (customer == null) {
                throw new RuntimeException("No customer selected");
            }
            FXMLLoader loader = openNormalWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml", borderPane);
            PaymentController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            controller.setBorderPane(borderPane);
            controller.setActiveUser(activeUser);
            controller.checkPayment(customer);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage(e.getMessage());
        }

    }

    public void fullInfoHandler() {
        Customers customer = tableView.getSelectionModel().getSelectedItem();
        try {
            if (customer == null) {
                throw new RuntimeException("No customer selected");
            }
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader loader = openNormalWindow("/com/example/gymmanagementsystem/newviews/info/customer-info.fxml", borderPane);
                CustomerInfoController controller = loader.getController();
                controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
                controller.setBorderPane(borderPane);
            }
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }

    }

    public void updateHandler() {
        try {
            Customers customer = tableView.getSelectionModel().getSelectedItem();
            if (customer == null) {
                throw new RuntimeException("No customer selected");
            }
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                FXMLLoader loader = openNormalWindow("/com/example/gymmanagementsystem/views/main-create/registrations.fxml", borderPane);
                RegistrationController controller = loader.getController();
                controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
                controller.setActiveUser(activeUser);
                controller.setBorderPane(borderPane);
            }
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }

    }

    public void deleteHandler() throws SQLException {
        Customers customer = tableView.getSelectionModel().getSelectedItem();

        if (customer != null) {

            try {
                ButtonType haa = new ButtonType("Haa", ButtonBar.ButtonData.YES);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inaad masaxdo macmiilkan "
                        , haa, new ButtonType("Maya", ButtonBar.ButtonData.NO));

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(haa)) {

                    CustomerService.deleteCustomer(customer);
                    infoAlert("Customer has been deleted successfully.");

                } else {
                    alert.close();
                }


            } catch (Exception e) {
                errorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        try {
            // TODO: 16/04/2023 Change to Data to fetch customers insha Allah
            customersList = CustomerService.fetchAllCustomer(activeUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //------------------------Helper methods-------------------
    private void initTable() {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(customers -> new SimpleStringProperty(customers.getValue().firstNameProperty().get() + "   " + customers.getValue().getMiddleName() + "   " + customers.getValue().getLastName()));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        weight.setCellValueFactory(customers -> new SimpleStringProperty(customers.getValue().getWeight() + "Kg"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        imagePath.setCellValueFactory(customers ->
                new SimpleStringProperty(customers.getValue().getImage() == null ? "X"
                        : "√"));
        status.setCellValueFactory(customers ->
                new SimpleStringProperty(!customers.getValue().getPayments().isEmpty() ? "(√)"
                        : "No payments"));

        waist.setCellValueFactory(customers ->
                new SimpleStringProperty(customers.getValue().getImage() == null ? "0"
                        : customers.getValue().getWaist() + "cm"));

        if (customersList.isEmpty()) {
            tableView.setPlaceholder(new Label("MACAAMIIL KUUMA DIWAAN GASHANA."));
        } else {
            tableView.setItems(customersList);
        }
    }

    private void searchFilter() {
        filteredList = new FilteredList<>(customersList, b -> true);
        SortedList<Customers> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        search.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(customer -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            if (customer.getFirstName().contains(newValue.toLowerCase()) || customer.getFirstName().contains(newValue.toUpperCase())) {
                return true;
            } else if (customer.getPhone().contains(newValue)) {
                return true;
            } else if (customer.getLastName().contains(newValue.toLowerCase()) || customer.getLastName().contains(newValue.toUpperCase())) {
                return true;
            } else
                return customer.getMiddleName().contains(newValue.toLowerCase()) || customer.getMiddleName().contains(newValue.toUpperCase());
        }));

    }


}
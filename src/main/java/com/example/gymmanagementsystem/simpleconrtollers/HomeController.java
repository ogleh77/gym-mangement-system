package com.example.gymmanagementsystem.simpleconrtollers;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
            //searchFilter();
            usersCount.setText(nextUserId == 1 ? nextUserId + " user" : nextUserId + " users");
            customersCount.setText(nextCustomerId == 1 ? nextCustomerId + " member" : nextCustomerId + " members");
            edahab.setText("eDahab: " + currentGym.geteDahab());
            zaad.setText("Zaad: " + currentGym.getZaad());
        });

    }

    @FXML
    void deleteHandler() {
        if (tableView.getSelectionModel().getSelectedItem() == null)
            Alerts.notificationAlert("No member selected ", "War-bixin").showAndWait();
        else {
            System.out.println("Good");
        }
    }

    @FXML
    void fullInfoHandler() {
        if (tableView.getSelectionModel().getSelectedItem() == null)
            Alerts.notificationAlert("No member selected ", "War-bixin").showAndWait();
        else {
            System.out.println("Good");
        }
    }

    @FXML
    void paymentHandler() {
        if (tableView.getSelectionModel().getSelectedItem() == null)
            Alerts.notificationAlert("No member selected ", "War-bixin").showAndWait();
        else {
            System.out.println("Good");
        }
    }

    @FXML
    void updateHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() == null)
            Alerts.notificationAlert("No member selected ", "War-bixin").showAndWait();
        else {
            FXMLLoader loader = OpenWindow.secondWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml", borderPane);
            HomeController controller = loader.getController();
            controller.setBorderPane(borderPane);
        }
    }


    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        try {
            super.setActiveUser(activeUser);
            customersList = CustomerService.fetchAllCustomer(UserService.users().get(0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

        if (customersList == null || customersList.isEmpty()) {
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

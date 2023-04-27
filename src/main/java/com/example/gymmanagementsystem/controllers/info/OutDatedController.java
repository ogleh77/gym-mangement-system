package com.example.gymmanagementsystem.controllers.info;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ResourceBundle;

public class OutDatedController extends CommonClass implements Initializable {
    @FXML
    public ComboBox<String> shift;
    @FXML
    private JFXRadioButton female;
    @FXML
    private DatePicker fromDate;
    @FXML
    private JFXRadioButton male;
    @FXML
    private Pagination pagination;
    @FXML
    private DatePicker toDate;
    @FXML
    private JFXButton searchHandler;
    @FXML
    private JFXRadioButton both;
    private int row = 0;
    private ObservableList<Customers> outDatedCustomers;
    private final ToggleGroup toggleGroup;

    public OutDatedController() {
        this.toggleGroup = new ToggleGroup();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            fromDate.setValue(LocalDate.now().minusDays(30));
            toDate.setValue(LocalDate.now());
            male.setToggleGroup(toggleGroup);
            female.setToggleGroup(toggleGroup);
            both.setToggleGroup(toggleGroup);
            shift.setItems(super.getShift());
            shift.getItems().add("All");
            shift.setValue("All");
            getMandatoryFields().addAll(fromDate, toDate);
        });

        service.setOnSucceeded(e -> {
            searchHandler.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-search-50.png"));
            searchHandler.setText("Search");
            pagination.setPageFactory(outDatedCustomers.isEmpty() ? this::vBox : this::createPage);
        });

    }

    @FXML
    void searchHandler() {
        if (isValid(getMandatoryFields(), null)) {
            startTask(service, searchHandler, "");
        }
    }

    private VBox vBox(int index) {
        VBox vBox = new VBox();
        Label label = new Label("Lama helin!");
        label.setStyle("-fx-font-size: 20; -fx-text-fill: #565252;-fx-font-family: Verdana");
        Label label1 = new Label("Fadlan ma jiro macamiil la helay oo leh astamaha aad ku baaadhay");
        label1.setStyle("-fx-font-size: 18; -fx-text-fill: #8a8989;-fx-font-family: Verdana");
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(label, label1);
        return vBox;
    }

    private GridPane createPage(int index) {
        int column = 0;

        int perPage = 6;
        int fromPage = perPage * index;
        int toIndex = Math.min(fromPage + perPage, outDatedCustomers.size());

        GridPane gridView = null;
        try {

            gridView = new GridPane();
            FXMLLoader loader;
            AnchorPane anchorPane;
            gridView.setPadding(new Insets(40, 50, 10, 50));

            for (int i = fromPage; i < toIndex; i++) {
                if (column == 2) {
                    column = 0;
                    row++;
                }

                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/gymmanagementsystem/newviews/info/customer-card.fxml"));
                anchorPane = loader.load();
                GridPane.setMargin(anchorPane, new Insets(10));
                CardController controller = loader.getController();
                controller.setCustomer(outDatedCustomers.get(i));
                gridView.add(anchorPane, column++, row);
            }

        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }

        return gridView;
    }

    private String customerQuery() {
        String ganderSelected = "";
        String query = "SELECT * FROM customers ";
        if (male.isSelected()) {
            ganderSelected = "Male";
        } else if (female.isSelected()) {
            ganderSelected = "Female";
        } else if (both.isSelected()) {
            ganderSelected = "";
        }

        if (activeUser.getRole().equals("super_admin")) {
            query += both.isSelected() ? "" : "WHERE gander='" + ganderSelected + "'";
        } else if (activeUser.getRole().equals("admin")) {
            query += " WHERE gander='" + activeUser.getGender() + "'";
        }

        if (!shift.getValue().equals("All") && activeUser.getRole().equals("super_admin")) {
            query += both.isSelected() ? "WHERE shift='" + shift.getValue() + "'" : " AND shift='" + shift.getValue() + "'";
        } else {
            query += (shift.getValue().equals("All")) ? "" : "AND shift='" + shift.getValue() + "'";
        }


        return query;
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (activeUser.getRole().equals("admin")) {
            male.setSelected(activeUser.getGender().equals("Male"));
            female.setSelected(activeUser.getGender().equals("Female"));
            male.setDisable(true);
            female.setDisable(true);
            both.setDisable(true);
        } else {
            both.setSelected(true);
        }
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    try {
                        String customerQuery = customerQuery();
                        outDatedCustomers = CustomerService.fetchQualifiedOfflineCustomers(customerQuery, fromDate.getValue(), toDate.getValue());
                        Collections.sort(outDatedCustomers);
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };
}

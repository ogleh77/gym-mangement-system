package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.controllers.DashboardController;
import com.example.gymmanagementsystem.data.dto.Data;
import com.example.gymmanagementsystem.data.dto.UserService;
import com.example.gymmanagementsystem.data.dto.main.CustomerService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.data.models.main.PaymentsModel;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SplashScreenController extends CommonClass implements Initializable {
    @FXML
    private ProgressBar progress;
    @FXML
    private Label waiting;
    @FXML
    private Label username;
    @FXML
    private ImageView loadingImage;
    @FXML
    private HBox topPane;
    private final ObservableList<Customers> warningList;
    private Stage stage;

    public SplashScreenController() {
        this.warningList = FXCollections.observableArrayList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) loadingImage.getScene().getWindow();
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageDropped(stage, topPane);
        });

        FetchOnlineCustomersByGander.setOnSucceeded(e -> {
            try {
                stage.close();
                openDashboard();
            } catch (IOException ex) {
                Alerts.errorAlert(ex.getMessage());
            }
        });
    }

    public Task<Void> FetchOnlineCustomersByGander = new Task<>() {
        private final LocalDate now = LocalDate.now();

        @Override
        protected Void call() {
            try {
                ObservableList<Customers> onlineCustomer = CustomerService.fetchAllOnlineCustomer(activeUser);
                int i = 0;
                int sleepTime = 100;

                for (Customers customer : onlineCustomer) {
                    i++;
                    updateMessage("checking for updates .. ");
                    updateProgress(i, onlineCustomer.size());
                    for (Payments payment : customer.getPayments()) {
                        LocalDate expDate = payment.getExpDate();
                        if (now.plusDays(2).isEqual(expDate) || now.plusDays(1).isEqual(expDate) || now.isEqual(expDate)) {
                            warningList.add(customer);
                        } else if (now.isAfter(payment.getExpDate())) {
                            PaymentsModel.offPayment(payment);
                        }
                    }
                    Thread.sleep(sleepTime);
                }
                ObservableList<Customers> customers = CustomerService.fetchAllCustomer(activeUser);
                ObservableList<Users> users = UserService.fetchAllUsers();

                updateMessage("fetching All customers");
                Data.setAllCustomersList(customers);
                Data.setAllUsers(users);

                Thread.sleep(1000);
                updateMessage("fetched successfully..");
            } catch (Exception e) {
                Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
            }
            return null;
        }
    };

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        Thread thread = new Thread(FetchOnlineCustomersByGander);
        thread.setDaemon(true);
        thread.start();
        progress.progressProperty().bind(FetchOnlineCustomersByGander.progressProperty());
        username.setText(activeUser.getUsername());
        waiting.textProperty().bind(FetchOnlineCustomersByGander.messageProperty());

        URL url = getClass().getResource(activeUser.getGender().equals("Male") ? images[1] : images[2]);
        Image image = new Image(String.valueOf(url));
        loadingImage.setImage(image);
    }

    private void openDashboard() throws IOException {
        FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/views/dashboard.fxml", topPane);
        DashboardController controller = loader.getController();
        controller.setWarningList(warningList);
        controller.setActiveUser(activeUser);
    }
}

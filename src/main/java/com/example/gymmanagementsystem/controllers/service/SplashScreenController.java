package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.dao.Data;
import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.example.gymmanagementsystem.simpleconrtollers.DashbaordController;
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
    private double xOffset = 0;
    private double yOffset = 0;

    public SplashScreenController() {
        this.warningList = FXCollections.observableArrayList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) loadingImage.getScene().getWindow();

            topPane.setOnMousePressed(event -> {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            });

            topPane.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            });
        });
        FetchOnlineCustomersByGander.setOnSucceeded(e -> {
            try {
                closeStage(stage, loadingImage.getParent());
                openDashboard();
            } catch (IOException ex) {
                errorMessage(ex.getMessage());
            }
        });
    }

    public Task<Void> FetchOnlineCustomersByGander = new Task<>() {
        private final LocalDate now = LocalDate.now();

        @Override
        protected Void call() {
            try {
                ObservableList<Customers> offlineCustomers = CustomerService.fetchAllOnlineCustomer(activeUser);
                int i = 0;
                int sleepTime = 100;

                for (Customers customer : offlineCustomers) {
                    i++;
                    updateMessage("checking for updates .. ");
                    updateProgress(i, offlineCustomers.size());
                    for (Payments payment : customer.getPayments()) {
                        LocalDate expDate = payment.getExpDate();
                        if (now.plusDays(2).isEqual(expDate) || now.plusDays(1).isEqual(expDate) || now.isEqual(expDate)) {
                            warningList.add(customer);
                        } else if (now.isAfter(payment.getExpDate())) {
                            // TODO: 17/04/2023 set the payment off
                            //PaymentModel.offPayment(payment);
                        }
                    }
                    Thread.sleep(sleepTime);
                }
                ObservableList<Customers> customers = CustomerService.fetchAllCustomer(activeUser);
                System.out.println(customers.size());

                updateMessage("fetching All customers");
                Data.setAllCustomersList(customers);
                Thread.sleep(1000);
                updateMessage("fetched successfully..");
            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorMessage(e.getMessage());
                });
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
        FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/redash/redash.fxml", null);
        DashbaordController controller = loader.getController();
        //controller.setWarningList(warningList);
        controller.setActiveUser(activeUser);
    }
}

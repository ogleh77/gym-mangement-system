package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.dao.Data;
import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML
    private Label message;
    private Users activeUser;

    public TestController() throws SQLException {
        this.activeUser = UserService.users().get(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            service.start();
            message.textProperty().bind(service.messageProperty());
        });
        service.setOnSucceeded(e -> System.out.println("good to go"));

    }


    @FXML
    void loadHandler() {

    }

    @FXML
    void openStageHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/views/secondWindow.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void showHashHandler() {
        System.out.println("customers " + Data.getAllCustomersList().hashCode());
        System.out.println("online customers " + Data.getOnlineCustomers().hashCode());

        System.out.println();
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    updateMessage("Loading customers");

                    Data.setAllCustomersList(CustomerService.fetchAllCustomer(activeUser));
                    Thread.sleep(1000);
                    updateMessage("Loading online customers");
                    Data.setOnlineCustomers(CustomerService.fetchAllOnlineCustomer(activeUser));
                    updateMessage("Done");
                    return null;
                }
            };
        }
    };
}

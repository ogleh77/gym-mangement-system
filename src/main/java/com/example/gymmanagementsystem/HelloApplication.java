package com.example.gymmanagementsystem;


import com.example.gymmanagementsystem.controllers.main.PaymentController;
import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.main.PaymentService;
import com.example.gymmanagementsystem.dao.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/newviews/main/payments.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PaymentController controller = fxmlLoader.getController();
      //  controller.setActiveUser(UserService.users().get(0));
        controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0));
        controller.setUpdatePayment(PaymentService.fetchAllPayments("8435290").get(0));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
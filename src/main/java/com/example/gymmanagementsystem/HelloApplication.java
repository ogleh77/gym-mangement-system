package com.example.gymmanagementsystem;


import com.example.gymmanagementsystem.controllers.DashboardController;
import com.example.gymmanagementsystem.controllers.main.HomeController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/newviews/service/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        DashboardController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(0));
//        OutDatedController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(4));
//        CustomerInfoController controller = fxmlLoader.getController();
//        Customers customer = CustomerService.fetchAllCustomer(UserService.users().get(0)).get(1);
//        customer.setPayments(PaymentService.fetchAllPayments(customer.getPhone()));
//        controller.setCustomer(customer);
        //UpdateUserController controller = fxmlLoader.getController();
        //controller.setUser(UserService.users().get(4));
        // controller.setActiveUser(UserService.users().get(4));
        //CustomerInfoController controller = fxmlLoader.getController();
        //  controller.setActiveUser(UserService.users().get(0));
        //   controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0));
        // controller.setUpdatePayment(PaymentService.fetchAllPayments("8435290").get(0));
        // TODO: 17/04/2023 Set payment off insha Allah customer delete open the delete
        // TODO: 17/04/2023 Payment controller re check insha Allah

        // TODO: 17/04/2023 Add registration daily report insha Allah
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}